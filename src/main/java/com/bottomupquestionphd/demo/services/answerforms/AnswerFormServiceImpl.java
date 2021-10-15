package com.bottomupquestionphd.demo.services.answerforms;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.bottomupquestionphd.demo.domains.dtos.answerform.CreateAnswerFormDTO;
import com.bottomupquestionphd.demo.domains.dtos.answerform.DisplayAllUserAnswersDTO;
import com.bottomupquestionphd.demo.domains.dtos.answerform.DisplayAnswersFromAnAnswerFormDTO;
import com.bottomupquestionphd.demo.domains.dtos.answerform.DisplayOneUserAnswersDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUsersQuestionFormsDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.answer.AnswerNotFoundByIdException;
import com.bottomupquestionphd.demo.exceptions.answerform.*;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionTypesAndQuestionTextsSizeMissMatchException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.AnswerFormRepository;
import com.bottomupquestionphd.demo.services.answers.AnswerService;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import com.bottomupquestionphd.demo.services.deleteservice.DeleteService;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import com.bottomupquestionphd.demo.services.validations.ErrorServiceImpl;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class AnswerFormServiceImpl implements AnswerFormService {
  private final AnswerFormRepository answerFormRepository;
  private final QuestionFormService questionFormService;
  private final AppUserService appUserService;
  private final AnswerService answerService;
  private final DeleteService deleteService;
  private final EntityManager entityManager;

  public AnswerFormServiceImpl(AnswerFormRepository answerFormRepository, QuestionFormService questionFormService, AppUserService appUserService, AnswerService answerService, DeleteService deleteService, EntityManager entityManager) {
    this.answerFormRepository = answerFormRepository;
    this.questionFormService = questionFormService;
    this.appUserService = appUserService;
    this.answerService = answerService;
    this.deleteService = deleteService;
    this.entityManager = entityManager;
  }

  @Override
  public CreateAnswerFormDTO createAnswerFormDTO(long questionFormId) throws QuestionFormNotFoundException, AnswerFormAlreadyFilledOutByCurrentUserException {
    QuestionForm questionForm = questionFormService.findByIdForAnswerForm(questionFormId);
    AppUser currentUser = appUserService.findCurrentlyLoggedInUser();
    checkIfUserHasFilledOutAnswerForm(questionForm, currentUser.getId());

    return new CreateAnswerFormDTO(0, questionFormId, currentUser.getId(), questionForm.getQuestions());
  }

  @Override
  public AnswerForm saveAnswerForm(AnswerForm answerForm, long questionFormId, long appUserId) throws NoSuchUserByIdException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException, AnswerFormAlreadyFilledOutByCurrentUserException {
    if (answerForm == null) {
      throw new MissingParamsException("AnswerForm can not be null");
    }
    AppUser appUser = appUserService.findById(appUserId);
    appUserService.checkIfCurrentUserMatchesUserIdInPath(appUserId);

    QuestionForm questionForm = questionFormService.findByIdForAnswerForm(questionFormId);
    checkIfUserHasFilledOutAnswerForm(questionForm, appUserId);
    appUser.addOneAnswerForm(answerForm);
    answerForm.setAppUser(appUser);
    answerForm.setQuestionForm(questionForm);
    checkForEmptyAnswerText(answerForm);

    answerService.connectAnswersToActualAnswers(answerForm.getAnswers());
    answerService.connectQuestionsToAnswers(answerForm.getAnswers(), questionForm.getId());
    connectAnswerFormToAnswers(answerForm);
    answerFormRepository.save(answerForm);
    return answerForm;
  }

  private void connectAnswerFormToAnswers(AnswerForm answerForm) {
    for (Answer answer : answerForm.getAnswers()) {
      answer.setAnswerForm(answerForm);
    }
  }

  private void checkForEmptyAnswerText(AnswerForm answerForm) throws MissingParamsException {
    for (Answer answer : answerForm.getAnswers()) {
      for (ActualAnswerText actualAnswerText : answer.getActualAnswerTexts()) {
        actualAnswerText.setAnswer(answer);
        ErrorServiceImpl.buildMissingFieldErrorMessage(actualAnswerText);
      }
    }
  }

  public AnswerForm findAnswerFormById(long answerFormId) throws NoSuchAnswerformById {
    return answerFormRepository.findById(answerFormId).orElseThrow(() -> new NoSuchAnswerformById("Couldn't find answerform belonging to the given id"));
  }

  @Override
  public List<AppUsersQuestionFormsDTO> findQuestionFormsFilledOutByAppUserId(long appUserId) throws BelongToAnotherUserException {
    appUserService.checkIfCurrentUserMatchesUserIdInPath(appUserId);
    return answerFormRepository.findAllQuestionFormsFilledOutByUser(appUserId);
  }

  @Override
  public List<Long> findQuestionFormIdsFilledOutByUser(long appUserId) {
    return answerFormRepository.findAllQuestionFormIdsFilledOutByAppUser(appUserId);
  }

  @Override
  @Transactional
  public CreateAnswerFormDTO createAnswerFormToUpdate(long questionFormId, long appUserId) throws BelongToAnotherUserException, QuestionFormNotFoundException, AnswerFormNotFilledOutException {
    appUserService.checkIfCurrentUserMatchesUserIdInPath(appUserId);
    QuestionForm questionForm = questionFormService.findByIdForAnswerForm(questionFormId);
    AnswerForm answerForm = questionForm.getAnswerForms()
            .stream()
            .filter(a -> a.getAppUser().getId() == appUserId)
            .findFirst().orElse(null);
    if (answerForm == null) {
      throw new AnswerFormNotFilledOutException("You need to fill out the answerform in order to update it");
    } else if (answerForm.getAnswers().size() < 1) {
      deleteService.setAnswerFormToBeDeleted(answerForm);
      throw new AnswerFormNotFilledOutException("You need to fill out the answerform in order to update it, you provided no valid answers");
    }
    sortAnswersByQuestions(questionForm.getQuestions(), answerForm.getAnswers());
    CreateAnswerFormDTO createAnswerFormDTO = new CreateAnswerFormDTO(answerForm.getId(), questionFormId, appUserId, questionForm.getQuestions(), answerForm.getAnswers());
    return createAnswerFormDTO;
  }

  @Override
  public List<Answer> sortAnswersByQuestions(List<Question> questions, List<Answer> answers) {
    Set<Answer> sortedAnswersByQuestions = new LinkedHashSet<>();
    for (int i = 0; i < questions.size(); i++) {
      Question q = questions.get(i);
      for (int j = 0; j < answers.size(); j++) {
        Answer a = answers.get(j);
        if (q.getId() == a.getQuestion().getId()) {
          sortedAnswersByQuestions.add(a);
          break;
        }
      }
    }
    answers.clear();
    answers.addAll(sortedAnswersByQuestions);
    return answers;
  }

  @Override
  @Transactional
  public AnswerForm saveUpdatedAnswerForm(long answerFormId, long appUserId, AnswerForm answerForm) throws NoSuchAnswerformById, BelongToAnotherUserException, MissingParamsException, NumberOfQuestionAndAnswersShouldMatchException, AnswerFormNumberOfAnswersShouldMatchException {
    if (answerForm == null) {
      throw new MissingParamsException("AnswerForm can not be null");
    }

    AnswerForm originalAnswerForm = findAnswerFormById(answerFormId);
    QuestionForm questionForm = originalAnswerForm.getQuestionForm();
    appUserService.checkIfCurrentUserMatchesUserIdInPath(appUserId);
    if (originalAnswerForm.getAppUser().getId() != appUserId) {
      throw new BelongToAnotherUserException("This answerForm belongs to another user");
    }
    answerForm.setAnswers(answerService.removeNullAnswerTextsFromAnswer(answerForm.getAnswers()));
    List<Answer> answersReturned = setActualAnswerTextsFromOriginalToUpdated(answerForm.getAnswers(), originalAnswerForm.getAnswers());
    setAnswerFormsAnswerToDeleted(originalAnswerForm.getAnswers());
    setQuestionsToAnswers(questionForm.getQuestions(), answersReturned);
    originalAnswerForm.getAnswers().addAll(answersReturned);
    setAnswersToAnswerForm(originalAnswerForm, originalAnswerForm.getAnswers());
    entityManager.merge(originalAnswerForm);
    entityManager.flush();
    return originalAnswerForm;
  }

  private void setAnswerFormsAnswerToDeleted(List<Answer> answers) {
    for (int i = 0; i < answers.size(); i++) {
      answers.get(i).setDeleted(true);
    }
  }

  private List<Answer> setActualAnswerTextsFromOriginalToUpdated(List<Answer> answers, List<Answer> originalAnswerFormAnswer) throws AnswerFormNumberOfAnswersShouldMatchException {
    if (answers.size() != originalAnswerFormAnswer.size()) {
      throw new AnswerFormNumberOfAnswersShouldMatchException("Updated AnswerForms number of answers and original AnswerForms number of answers should match");
    }
    for (int i = 0; i < answers.size(); i++) {
      Answer answer = originalAnswerFormAnswer.get(i);
      List<ActualAnswerText> filteredActualAnswerTexts = findMatchingActualAnswerTexts(answers.get(i), originalAnswerFormAnswer.get(i));
      answer.setActualAnswerTexts(filteredActualAnswerTexts);
    }
    return answers;
  }

  private List<ActualAnswerText> findMatchingActualAnswerTexts(Answer answer, Answer originalAnswerFormAnswer) {
    List<ActualAnswerText> filteredAnswerTexts = new ArrayList<>();
    for (int i = 0; i < answer.getActualAnswerTexts().size(); i++) {
      ActualAnswerText updated = answer.getActualAnswerTexts().get(i);
      for (int j = 0; j < originalAnswerFormAnswer.getActualAnswerTexts().size(); j++) {
        ActualAnswerText original = originalAnswerFormAnswer.getActualAnswerTexts().get(j);
        if (updated.equals(original)) {
          original.setAnswer(answer);
          filteredAnswerTexts.add(original);
          answer.getActualAnswerTexts().remove(original);
          originalAnswerFormAnswer.getActualAnswerTexts().remove(original);
        }
      }
    }
    if (answer.getActualAnswerTexts().size() > 0) {
      for (int i = 0; i < answer.getActualAnswerTexts().size(); i++) {
        answer.getActualAnswerTexts().get(i).setAnswer(answer);
      }
      filteredAnswerTexts.addAll(answer.getActualAnswerTexts());
    }
    return filteredAnswerTexts;
  }

  private void setQuestionsToAnswers(List<Question> questions, List<Answer> answers) throws NumberOfQuestionAndAnswersShouldMatchException {
    if (questions.size() != answers.size()) {
      throw new NumberOfQuestionAndAnswersShouldMatchException("Number of questions and the provided number of answers do not match");
    }
    for (int i = 0; i < questions.size(); i++) {
      answers.get(i).setQuestion(questions.get(i));
    }
  }

  private void setAnswersToAnswerForm(AnswerForm originalAnswerForm, List<Answer> answers) {
    for (int i = 0; i < answers.size(); i++) {
      answers.get(i).setAnswerForm(originalAnswerForm);
    }
  }

  @Override
  public DisplayAnswersFromAnAnswerFormDTO findAnswerFormByAnswerId(long answerId) throws AnswerNotFoundByIdException, NoSuchAnswerformById, BelongToAnotherUserException {
    Answer actualAnswer = answerService.findById(answerId);
    AnswerForm answerForm = findAnswerFormById(actualAnswer.getAnswerForm().getId());
    appUserService.checkIfCurrentUserMatchesUserIdInPath(answerForm.getAppUser().getId());
    DisplayAnswersFromAnAnswerFormDTO displayAnswersFromAnAnswerFormDTO = new DisplayAnswersFromAnAnswerFormDTO();
    for (Answer answer : answerForm.getAnswers()) {
      displayAnswersFromAnAnswerFormDTO.getAnswers().add(answer);
      displayAnswersFromAnAnswerFormDTO.getQuestionTypes().add(answer.getQuestion().getDiscriminatorValue());
      displayAnswersFromAnAnswerFormDTO.getQuestionTexts().add(answer.getQuestion().getQuestionText());
    }
    return displayAnswersFromAnAnswerFormDTO;
  }

  @Override
  public DisplayAllUserAnswersDTO findAllAnswersBelongingToQuestionForm(long questionFormId, long appUserId) throws QuestionFormNotFoundException, BelongToAnotherUserException, MissingUserException, QuestionTypesAndQuestionTextsSizeMissMatchException, AnswerFormNotFilledOutException {
    QuestionForm questionForm = questionFormService.findById(questionFormId);
    appUserService.checkIfCurrentUserMatchesUserIdInPath(appUserId);
    if (questionForm.getAnswerForms() == null || questionForm.getAnswerForms().isEmpty()) {
      throw new AnswerFormNotFilledOutException("No user filled out the answer form");
    }
    DisplayAllUserAnswersDTO displayAllUserAnswersDTO = new DisplayAllUserAnswersDTO(questionForm, aggregateAllAnswerTextBelongingToOneQuestions(questionForm.getAnswerForms()));
    return displayAllUserAnswersDTO;
  }


  private List<List<String>> aggregateAllAnswerTextBelongingToOneQuestions(List<AnswerForm> answerForms) {
    List<List<String>> aggregatedAnswerTextsBelongingToOneAnswers = new ArrayList<>();
    for (AnswerForm answerForm : answerForms) {
      List<String> temp = new ArrayList<>();
      for (Answer answer : answerForm.getAnswers()) {
        temp.add(answer.getActualAnswerInAString());
      }
      aggregatedAnswerTextsBelongingToOneAnswers.add(temp);
    }
    return aggregatedAnswerTextsBelongingToOneAnswers;
  }

  @Override
  public DisplayOneUserAnswersDTO findAllAnswersBelongingToAnUser(long questionFormId) throws QuestionFormNotFoundException, NoSuchAnswerformById, AnswerFormNotFilledOutException {
    QuestionForm questionForm = questionFormService.findByIdForAnswerForm(questionFormId);
    if (questionForm.getAnswerForms() == null || questionForm.getAnswerForms().isEmpty()) {
      throw new AnswerFormNotFilledOutException("No user filled out the answer form");
    }
    AppUser appUser = appUserService.findCurrentlyLoggedInUser();
    AnswerForm answerForm = questionForm.getAnswerForms()
            .stream()
            .filter(a -> a.getAppUser().getId() == appUser.getId())
            .findFirst()
            .orElse(null);
    if (answerForm == null) {
      throw new NoSuchAnswerformById("Couldn't find the required answerform");
    }

    List<String> filteredAnswers = aggregateAllAnswerTextBelongingToAnswerForm(answerForm);

    DisplayOneUserAnswersDTO displayOneUserAnswersDTO = new DisplayOneUserAnswersDTO(filteredAnswers, questionForm.getQuestionTexts());
    return displayOneUserAnswersDTO;
  }

  private List<String> aggregateAllAnswerTextBelongingToAnswerForm(AnswerForm answerForm) {
    List<String> aggregatedAnswerTextsBelongingToOneAnswersForms = new ArrayList<>();
    for (Answer answer : answerForm.getAnswers()) {
      aggregatedAnswerTextsBelongingToOneAnswersForms.add(answer.getActualAnswerInAString());
    }
    return aggregatedAnswerTextsBelongingToOneAnswersForms;
  }

  private void checkIfUserHasFilledOutAnswerForm(QuestionForm questionForm, long appUserId) throws QuestionFormNotFoundException, AnswerFormAlreadyFilledOutByCurrentUserException {
    if (questionForm == null) {
      throw new QuestionFormNotFoundException("Couldn't find question form");
    }
    boolean hasUserFilledOutGivenAnswerForm =
            questionForm
                    .getAnswerForms()
                    .stream()
                    .filter(form -> form.getAppUser() != null)
                    .anyMatch(form -> form.getAppUser().getId() == appUserId);
    if (hasUserFilledOutGivenAnswerForm) {
      throw new AnswerFormAlreadyFilledOutByCurrentUserException("You have already filled out this question form, please update the one where you filled it out");
    }
  }
}
