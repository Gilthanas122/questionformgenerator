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
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUsersQuestionFormsDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.answer.AnswerNotFoundByIdException;
import com.bottomupquestionphd.demo.exceptions.answerform.AnswerFormAlreadyFilledOutByCurrentUserException;
import com.bottomupquestionphd.demo.exceptions.answerform.AnswerFormNotFilledOutException;
import com.bottomupquestionphd.demo.exceptions.answerform.NoSuchAnswerformById;
import com.bottomupquestionphd.demo.exceptions.answerform.NoUserFilledOutAnswerFormException;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionTypesAndQuestionTextsSizeMissMatchException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.AnswerFormRepository;
import com.bottomupquestionphd.demo.services.answers.AnswerService;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import com.bottomupquestionphd.demo.services.validations.ErrorServiceImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerFormServiceImpl implements AnswerFormService {
  private final AnswerFormRepository answerFormRepository;
  private final QuestionFormService questionFormService;
  private final AppUserService appUserService;
  private final AnswerService answerService;

  public AnswerFormServiceImpl(AnswerFormRepository answerFormRepository, QuestionFormService questionFormService, AppUserService appUserService, AnswerService answerService) {
    this.answerFormRepository = answerFormRepository;
    this.questionFormService = questionFormService;
    this.appUserService = appUserService;
    this.answerService = answerService;
  }


  // SHOULD RE-TEST IT
  @Override
  public CreateAnswerFormDTO createAnswerFormDTO(long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, AnswerFormAlreadyFilledOutByCurrentUserException {
    QuestionForm questionForm = questionFormService.findByIdForAnswerForm(questionFormId);
    AppUser currentUser = appUserService.findCurrentlyLoggedInUser();
    checkIfUserHasFilledOutAnswerForm(questionForm, currentUser.getId());
    List<Question> questionsWithTextQuestionsAnswersNotFilledOutByCurrentUser = answerService.addActualTextAnswersNotFilledOutByUser(questionForm.getQuestions(), currentUser.getId());

    CreateAnswerFormDTO createAnswerFormDTO = new CreateAnswerFormDTO(0, questionFormId, currentUser.getId(), questionsWithTextQuestionsAnswersNotFilledOutByCurrentUser);
    return createAnswerFormDTO;
  }

  @Override
  public AnswerForm saveAnswerForm(AnswerForm answerForm, long questionFormId, long appUserId) throws NoSuchUserByIdException, MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException, AnswerFormAlreadyFilledOutByCurrentUserException {
    if (answerForm == null) {
      throw new MissingParamsException("Answer Form can not be null");
    }
    appUserService.checkIfCurrentUserMatchesUserIdInPath(appUserId);
    AppUser appUser = appUserService.findById(appUserId);

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
  public CreateAnswerFormDTO createAnswerFormToUpdate(long questionFormId, long answerFormId, long appUserId) throws BelongToAnotherUserException, QuestionFormNotFoundException, MissingUserException, AnswerFormNotFilledOutException, MissingParamsException {
    appUserService.checkIfCurrentUserMatchesUserIdInPath(appUserId);
    QuestionForm questionForm = questionFormService.findByIdForAnswerForm(questionFormId);
    AnswerForm answerForm = questionForm.getAnswerForms()
            .stream()
            .filter(a -> a.getId() == answerFormId)
            .findFirst().orElse(null);
    if (answerForm == null) {
      throw new AnswerFormNotFilledOutException("You need to fill out the answerform in order to update it");
    }

    CreateAnswerFormDTO createAnswerFormDTO = new CreateAnswerFormDTO(answerFormId, questionFormId, appUserId, questionForm.getQuestions(), answerForm.getAnswers());
    return createAnswerFormDTO;
  }

  @Override
  public AnswerForm saveUpdatedAnswerForm(long answerFormId, long appUserId, AnswerForm answerForm) throws NoSuchAnswerformById, BelongToAnotherUserException, MissingParamsException {
    AnswerForm originalAnswerForm1 = findAnswerFormById(answerFormId);
    if (originalAnswerForm1.getAppUser().getId() != appUserId) {
      throw new BelongToAnotherUserException("This answerForm belongs to another user");
    }
    answerForm.setAppUser(originalAnswerForm1.getAppUser());
    answerForm.setQuestionForm(originalAnswerForm1.getQuestionForm());
    answerForm.setId(originalAnswerForm1.getId());
    answerForm.setAnswers(answerService.removeNullAnswerTextsFromAnswer(answerForm.getAnswers(), answerForm, originalAnswerForm1.getAnswers(), this));
    answerFormRepository.save(answerForm);
    return answerForm;
  }

  //NOT TESTED
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

  //NOT TESTED
  @Override
  public DisplayAllUserAnswersDTO findAllAnswersBelongingToQuestionForm(long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, NoUserFilledOutAnswerFormException, QuestionTypesAndQuestionTextsSizeMissMatchException {
    QuestionForm questionForm = questionFormService.findById(questionFormId);
    if (questionForm.getAnswerForms() == null || questionForm.getAnswerForms().isEmpty()) {
      throw new NoUserFilledOutAnswerFormException("No user filled out the answer form");
    }
    DisplayAllUserAnswersDTO displayAllUserAnswersDTO = new DisplayAllUserAnswersDTO(questionForm, aggregateAllAnswerTextBelongingToOneQuestions(questionForm.getAnswerForms()));
    return displayAllUserAnswersDTO;
  }



  //NOT TESTED
  public List<List<String>> aggregateAllAnswerTextBelongingToOneQuestions(List<AnswerForm> answerForms) {
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
