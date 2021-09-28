package com.bottomupquestionphd.demo.services.answers;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.answer.AnswerNotFoundByIdException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.AnswerRepository;
import com.bottomupquestionphd.demo.services.actualanswertexts.ActualAnswerTextService;
import com.bottomupquestionphd.demo.services.answerforms.AnswerFormServiceImpl;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class AnswerServiceImpl implements AnswerService {
  private final AnswerRepository answerRepository;
  private final QuestionFormService questionFormService;
  private final ActualAnswerTextService actualAnswerTextService;

  public AnswerServiceImpl(AnswerRepository answerRepository, QuestionFormService questionFormService, ActualAnswerTextService actualAnswerTextService) {
    this.answerRepository = answerRepository;
    this.questionFormService = questionFormService;
    this.actualAnswerTextService = actualAnswerTextService;
  }

  @Override
  public void connectQuestionsToAnswers(List<Answer> answers, long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException {
    if (answers == null) {
      throw new MissingParamsException("Answers can not be null");
    }
    QuestionForm questionForm = questionFormService.findByIdForAnswerForm(questionFormId);
    for (int i = 0; i < answers.size(); i++) {
      answers.get(i).setQuestion(questionForm.getQuestions().get(i));
      questionForm.getQuestions().get(i).addOneAnswer(answers.get(i));
    }
  }

  @Override
  public void connectAnswersToActualAnswers(List<Answer> answers) throws MissingParamsException {
    if (answers == null) {
      throw new MissingParamsException("Answers can not be null");
    }
    for (int i = 0; i < answers.size(); i++) {
      Answer currentAnswer = answers.get(i);
      for (int j = 0; j < answers.get(i).getActualAnswerTexts().size(); j++) {
        ActualAnswerText currentActualAnswer = currentAnswer.getActualAnswerTexts().get(j);
        currentActualAnswer.setAnswer(currentAnswer);
      }
    }
  }

  @Override
  public void setActualAnswersToDeleted(long appUserId, long questionFormId) throws MissingParamsException {
    List<Long> answerIdsToBeDeleted = answerRepository.findAllAnswerToBeDeleted(appUserId);
    answerRepository.setAnswerToDeletedByQuestionFormId(questionFormId);
    actualAnswerTextService.setToDeleted(answerIdsToBeDeleted);
  }

  @Override
  public List<Answer> setAnswersToAnswerForm(AnswerForm answerForm, List<Answer> answers, List<Answer> originalAnswerFormsAnswers) throws MissingParamsException {
    if (answerForm == null || answers == null || originalAnswerFormsAnswers == null) {
      throw new MissingParamsException("Either answerform or answers or original answers can not be null");
    }
    for (int i = 0; i < answers.size(); i++) {
      answers.get(i).setAnswerForm(answerForm);
      answers.get(i).setQuestion(originalAnswerFormsAnswers.get(i).getQuestion());
      answers.get(i).setId(originalAnswerFormsAnswers.get(i).getId());
    }
    answers = actualAnswerTextService.setActualAnswerTextsToAnswer(answers, originalAnswerFormsAnswers);
    return answers;
  }

  @Override
  public List<Answer> findAllAnswerTextsBelongingToAQuestion(List<Long> questionIds) throws MissingParamsException {
    if (questionIds == null) {
      throw new MissingParamsException("Question ids can not be null");
    }
    return answerRepository.findAllByQuestionIds(questionIds);
  }

  @Override
  public List<Answer> removeOwnAATextsFromAATToBeVoted(long appUserId, List<Answer> allActualAnswerTextsBelongingToAQuestion) throws MissingParamsException {
    if (allActualAnswerTextsBelongingToAQuestion == null) {
      throw new MissingParamsException("All actual answers belonging to the user can not be null");
    }
    List<Answer> answers =
            allActualAnswerTextsBelongingToAQuestion
                    .stream()
                    .filter(a -> a.getAnswerForm().getAppUser().getId() != appUserId)
                    .filter(a -> a.getActualAnswerTexts().size() > 0)
                    .collect(Collectors.toList());
    return answers;
  }

  @Override
  public List<Answer> removeNullAnswerTextsFromAnswer(List<Answer> answers, AnswerForm answerForm, List<Answer> originalFormsAnswers, AnswerFormServiceImpl answerFormService) throws MissingParamsException {
    for (Answer answer : answers) {
      Predicate<ActualAnswerText> isNotEmptyOrNull = item -> !item.actualAnswerTextIsNullOrEmpty();
      List<ActualAnswerText> answerTextOfAnswer = answer.getActualAnswerTexts();
      List<ActualAnswerText> actualAnswerTextsFiltered = new ArrayList<>();

      answer
              .getActualAnswerTexts()
              .stream()
              .filter(isNotEmptyOrNull)
              .forEach(item -> {
                actualAnswerTextsFiltered.add(item);
              });
      answer.setActualAnswerTexts(answerTextOfAnswer);
    }
    answers = setAnswersToAnswerForm(answerForm, answers, originalFormsAnswers);
    return answers;
  }

  //NOT TESTED
  @Override
  public Answer findById(long answerId) throws AnswerNotFoundByIdException {
    return answerRepository.findById(answerId).orElseThrow(() -> new AnswerNotFoundByIdException("Couldn't find answer with the given id"));
  }

  //NOT TESTED
  @Override
  public List<Question> addActualTextAnswersNotFilledOutByUser(List<Question> questions, long appUserId) {
    List<Question> questionsAddedActualAnswerTextsNotFilledOutByUser = new ArrayList<>();
    for (int i = 0; i <questions.size(); i++) {
      Question q = questions.get(i);
      if (q.getDiscriminatorValue().equals("TextQuestion")){
        for (int j = 0; j <q.getAnswers().size(); j++) {
          Answer a = q.getAnswers().get(j);
          if (a.getAnswerForm().getAppUser().getId() != appUserId){
            q.setAnswersNotFilledOutByUser(a.getActualAnswerInList());
          }
        }
      }
      questionsAddedActualAnswerTextsNotFilledOutByUser.add(q);
    }
    return questionsAddedActualAnswerTextsNotFilledOutByUser;
  }
}
