package com.bottomupquestionphd.demo.unittests.answer;

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
import com.bottomupquestionphd.demo.exceptions.answerform.*;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionTypesAndQuestionTextsSizeMissMatchException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.AnswerFormRepository;
import com.bottomupquestionphd.demo.services.answers.answerforms.AnswerFormService;
import com.bottomupquestionphd.demo.services.answers.answerforms.AnswerFormServiceImpl;
import com.bottomupquestionphd.demo.services.answers.AnswerService;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import com.bottomupquestionphd.demo.services.deleteservice.DeleteService;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import com.bottomupquestionphd.demo.testconfiguration.TestConfigurationBeanFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
public class AnswerFormServiceTest {
  private final AnswerFormRepository answerFormRepository = Mockito.mock(AnswerFormRepository.class);
  private final QuestionFormService questionFormService = Mockito.mock(QuestionFormService.class);
  private final AppUserService appUserService = Mockito.mock(AppUserService.class);
  private final AnswerService answerService = Mockito.mock(AnswerService.class);
  private final DeleteService deleteService = Mockito.mock(DeleteService.class);
  private final EntityManager entityManager = Mockito.mock(EntityManager.class);

  @Autowired
  private BeanFactory beanFactory;
  private AnswerFormService answerFormService;

  @Before
  public void setup() {
    answerFormService = new AnswerFormServiceImpl(answerFormRepository, questionFormService, appUserService, answerService, deleteService, entityManager);
  }

  @Test
  public void createAnswerFormDTO_withValidInput() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, AnswerFormAlreadyFilledOutByCurrentUserException {

    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    long questionFormId = 1;
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    CreateAnswerFormDTO createAnswerFormDTO = (CreateAnswerFormDTO) beanFactory.getBean("createAnswerFormDTO");

    Mockito.when(questionFormService.findByIdForAnswerForm(questionFormId)).thenReturn(questionForm);
    Mockito.when(appUserService.findCurrentlyLoggedInUser()).thenReturn(appUser);

    CreateAnswerFormDTO returnedCreateAnswerFormDTO = answerFormService.createAnswerFormDTO(questionFormId);

    Assert.assertEquals(appUser.getId(), returnedCreateAnswerFormDTO.getAppUserId());
    Assert.assertEquals(questionFormId, createAnswerFormDTO.getQuestionFormId());
    Assert.assertEquals(questionForm.getQuestions().size(), createAnswerFormDTO.getQuestions().size());
  }

  @Test(expected = AnswerFormAlreadyFilledOutByCurrentUserException.class)
  public void createAnswerFormDTO_withUserThatHasAlreadyFilledOutThQuestionForm_throwsAnswerFormAlreadyFilledOutException() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, AnswerFormAlreadyFilledOutByCurrentUserException {

    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionFormWithAnswerForm");
    long questionFormId = 1;
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setId(questionForm.getAnswerForms().get(0).getAppUser().getId());

    Mockito.when(questionFormService.findByIdForAnswerForm(questionFormId)).thenReturn(questionForm);
    Mockito.when(appUserService.findCurrentlyLoggedInUser()).thenReturn(appUser);

    answerFormService.createAnswerFormDTO(questionFormId);
  }

  @Test(expected = QuestionFormNotFoundException.class)
  public void createAnswerFormDTO_withUserThatHasAlreadyFilledOutThQuestionForm_shouldThrowQuestionFormNotFoundException() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, AnswerFormAlreadyFilledOutByCurrentUserException {

    QuestionForm questionForm = null;
    long questionFormId = 1;
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setId(1L);

    Mockito.when(questionFormService.findByIdForAnswerForm(questionFormId)).thenReturn(questionForm);
    Mockito.when(appUserService.findCurrentlyLoggedInUser()).thenReturn(appUser);

    answerFormService.createAnswerFormDTO(questionFormId);
  }

  @Test
  public void saveAnswerForm_withValidInputData() throws QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException, MissingUserException, NoSuchUserByIdException, AnswerFormAlreadyFilledOutByCurrentUserException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    AnswerForm answerForm = (AnswerForm) beanFactory.getBean("answerForm");
    long questionFormId = 1;
    long appUserId = 1;
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");

    Mockito.when(appUserService.findById(appUserId)).thenReturn(appUser);
    Mockito.when(questionFormService.findByIdForAnswerForm(questionFormId)).thenReturn(questionForm);
    answerFormService.saveAnswerForm(answerForm, questionFormId, appUserId);

    Mockito.verify(answerFormRepository, times(1)).save(answerForm);
    Assert.assertEquals(answerForm.getAnswers().get(0).getAnswerForm(), answerForm);
  }

  @Test(expected = MissingParamsException.class)
  public void saveAnswerForm_withNullInput_throwsMissingParamsException() throws QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException, MissingUserException, NoSuchUserByIdException, AnswerFormAlreadyFilledOutByCurrentUserException {
    AnswerForm answerForm = null;
    long questionFormId = 1;
    long appUserId = 1;
    answerFormService.saveAnswerForm(answerForm, questionFormId, appUserId);
  }

  @Test(expected = MissingParamsException.class)
  public void saveAnswerForm_withEmptyAnswerText_throwsMissingParamsException() throws QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException, MissingUserException, NoSuchUserByIdException, AnswerFormAlreadyFilledOutByCurrentUserException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    AnswerForm answerForm = (AnswerForm) beanFactory.getBean("answerForm");
    answerForm.getAnswers().get(0).getActualAnswerTexts().get(0).setAnswerText(null);
    long questionFormId = 1;
    long appUserId = 1;
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");

    Mockito.when(appUserService.findById(appUserId)).thenReturn(appUser);
    Mockito.when(questionFormService.findByIdForAnswerForm(questionFormId)).thenReturn(questionForm);
    answerFormService.saveAnswerForm(answerForm, questionFormId, appUserId);

    Mockito.verify(answerFormRepository, times(1)).save(answerForm);
    Assert.assertEquals(answerForm.getAnswers().get(0).getAnswerForm(), answerForm);
  }

  @Test(expected = AnswerFormAlreadyFilledOutByCurrentUserException.class)
  public void saveAnswerForm_withAppUserThatHasAlreadyFilledOutTheAnswerForm_throwsAnswerFormAlreadyFilledOutException() throws QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException, MissingUserException, NoSuchUserByIdException, AnswerFormAlreadyFilledOutByCurrentUserException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    AnswerForm answerForm = (AnswerForm) beanFactory.getBean("answerForm");
    long questionFormId = 1;
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionFormWithAnswerForm");
    long appUserId = questionForm.getAnswerForms().get(0).getAppUser().getId();

    Mockito.when(appUserService.findById(appUserId)).thenReturn(appUser);
    Mockito.when(questionFormService.findByIdForAnswerForm(questionFormId)).thenReturn(questionForm);
    answerFormService.saveAnswerForm(answerForm, questionFormId, appUserId);

    Mockito.verify(answerFormRepository, times(1)).save(answerForm);
    Assert.assertEquals(answerForm.getAnswers().get(0).getAnswerForm(), answerForm);
  }

  @Test(expected = QuestionFormNotFoundException.class)
  public void saveAnswerForm_withInvalidQuestionFormId_shouldThrowsQuestionFormNotFoundException() throws QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException, MissingUserException, NoSuchUserByIdException, AnswerFormAlreadyFilledOutByCurrentUserException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    AnswerForm answerForm = (AnswerForm) beanFactory.getBean("answerForm");
    long questionFormId = 1;
    long appUserId = 1L;

    Mockito.when(appUserService.findById(appUserId)).thenReturn(appUser);
    Mockito.when(questionFormService.findByIdForAnswerForm(questionFormId)).thenReturn(null);
    answerFormService.saveAnswerForm(answerForm, questionFormId, appUserId);
  }

  @Test
  public void findAnswerFormById_withValidInput_returnsAnswerform() throws NoSuchAnswerformById {
    long answerFormId = 1;
    AnswerForm answerForm = (AnswerForm) beanFactory.getBean("answerForm");

    Mockito.when(answerFormRepository.findById(answerFormId)).thenReturn(java.util.Optional.of(answerForm));

    AnswerForm returnedAnswerForm = answerFormService.findAnswerFormById(answerFormId);
    Assert.assertEquals(answerForm.getId(), returnedAnswerForm.getId());
  }

  @Test(expected = NoSuchAnswerformById.class)
  public void findAnswerFormById_withNonExistingAnswerFormId_throwsNoSuchAnswerFormById() throws NoSuchAnswerformById {
    long answerFormId = 1;

    given(answerFormRepository.findById(answerFormId)).willAnswer(invocation -> {
              throw new NoSuchAnswerformById("Ooops");
            }
    );

    answerFormService.findAnswerFormById(answerFormId);
  }

  @Test
  public void saveUpdatedAnswerForm_withValidInput() throws BelongToAnotherUserException, NoSuchUserByIdException, MissingParamsException, NoSuchAnswerformById, NumberOfQuestionAndAnswersShouldMatchException, AnswerFormNumberOfAnswersShouldMatchException {
    AnswerForm answerForm = (AnswerForm) beanFactory.getBean("answerForm");
    long answerFormId = answerForm.getId();
    long appUserId = answerForm.getAppUser().getId();

    Mockito.when(answerFormRepository.findById(answerFormId)).thenReturn(java.util.Optional.of(answerForm));
    Mockito.when(answerService.removeNullAnswerTextsFromAnswer(answerForm.getAnswers())).thenReturn(answerForm.getAnswers());

    answerFormService.saveUpdatedAnswerForm(answerFormId, appUserId, answerForm);

    Mockito.verify(entityManager, times(1)).merge(answerForm);
    Mockito.verify(entityManager, times(1)).flush();
  }

  @Test(expected = NumberOfQuestionAndAnswersShouldMatchException.class)
  public void saveUpdatedAnswerForm_withNonMatchingNumberOfAnswersAndQuestion_shouldThrowNumberOfQuestionAndAnswersShouldMatchException() throws BelongToAnotherUserException, NoSuchUserByIdException, MissingParamsException, NoSuchAnswerformById, NumberOfQuestionAndAnswersShouldMatchException, AnswerFormNumberOfAnswersShouldMatchException {
    AnswerForm answerForm = (AnswerForm) beanFactory.getBean("answerForm");
    long answerFormId = answerForm.getId();
    long appUserId = answerForm.getAppUser().getId();

    Mockito.when(answerFormRepository.findById(answerFormId)).thenReturn(java.util.Optional.of(answerForm));
    Mockito.when(answerService.removeNullAnswerTextsFromAnswer(answerForm.getAnswers())).thenReturn(new ArrayList<Answer>() {
    });

    answerFormService.saveUpdatedAnswerForm(answerFormId, appUserId, answerForm);
  }

  @Test(expected = BelongToAnotherUserException.class)
  public void saveUpdatedAnswerForm_withNotMatchingAppUserId_throwsBelongsToAnotherUserException() throws BelongToAnotherUserException, NoSuchUserByIdException, MissingParamsException, NoSuchAnswerformById, NumberOfQuestionAndAnswersShouldMatchException, AnswerFormNumberOfAnswersShouldMatchException {
    long appUserId = 12;
    AnswerForm answerForm = (AnswerForm) beanFactory.getBean("answerForm");
    long answerFormId = answerForm.getId();

    Mockito.when(answerFormRepository.findById(answerFormId)).thenReturn(java.util.Optional.of(answerForm));

    answerFormService.saveUpdatedAnswerForm(answerFormId, appUserId, answerForm);
  }

  @Test(expected = NoSuchAnswerformById.class)
  public void saveUpdatedAnswerForm_withNonExistentAnswerFormId_shouldThrowAnswerFormNotFoundException() throws BelongToAnotherUserException, NoSuchUserByIdException, MissingParamsException, NoSuchAnswerformById, NumberOfQuestionAndAnswersShouldMatchException, AnswerFormNumberOfAnswersShouldMatchException {
    AnswerForm answerForm = (AnswerForm) beanFactory.getBean("answerForm");
    long answerFormId = answerForm.getId();
    long appUserId = answerForm.getAppUser().getId();

    given(answerFormRepository.findById(answerForm.getId())).willAnswer(invocation -> {
      throw new NoSuchAnswerformById("Couldn't find answer with the given id");
    });

    answerFormService.saveUpdatedAnswerForm(answerFormId, appUserId, answerForm);
  }

  @Test(expected = MissingParamsException.class)
  public void saveUpdatedAnswerForm_withNullAnswerForm_shouldThrowMissingParamsException() throws BelongToAnotherUserException, NoSuchUserByIdException, MissingParamsException, NoSuchAnswerformById, NumberOfQuestionAndAnswersShouldMatchException, AnswerFormNumberOfAnswersShouldMatchException {
    AnswerForm answerForm = null;

    answerFormService.saveUpdatedAnswerForm(1l, 1l, answerForm);
  }

  @Test
  public void findQuestionFormsFilledOutByAppUserId_withValidData() throws BelongToAnotherUserException {
    long appUserId = 1L;
    List<AppUsersQuestionFormsDTO> appUsersQuestionFormsDTOs = new ArrayList<>();
    Mockito.when(answerFormRepository.findAllQuestionFormsFilledOutByUser(appUserId)).thenReturn(appUsersQuestionFormsDTOs);

    List<AppUsersQuestionFormsDTO> appUsersQuestionFormsDTOsReturned = answerFormService.findQuestionFormsFilledOutByAppUserId(appUserId);
    assertEquals(0, appUsersQuestionFormsDTOsReturned.size());
    Mockito.verify(answerFormRepository, times(1)).findAllQuestionFormsFilledOutByUser(appUserId);
  }

  @Test
  public void findQuestionFormIdsFilledOutByUser_withValidData() {
    List<Long> questionFormIds = List.of(1L, 2L, 3L);
    long appUserId = 1L;

    Mockito.when(answerFormRepository.findAllQuestionFormIdsFilledOutByAppUser(appUserId)).thenReturn(questionFormIds);

    List<Long> questionFormIdsReturned = answerFormService.findQuestionFormIdsFilledOutByUser(appUserId);

    assertEquals(3, questionFormIdsReturned.size());
  }

  @Test
  public void createAnswerFormToUpdate_withValidData() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, AnswerFormNotFilledOutException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionFormWithAnswerForm");
    long appUserId = questionForm.getAnswerForms().get(0).getAppUser().getId();

    Mockito.when(questionFormService.findByIdForAnswerForm(questionForm.getId())).thenReturn(questionForm);

    CreateAnswerFormDTO createAnswerFormDTOReturned = answerFormService.createAnswerFormToUpdate(questionForm.getId(), appUserId);

    assertEquals(createAnswerFormDTOReturned.getAppUserId(), appUserId);
    assertEquals(createAnswerFormDTOReturned.getQuestionFormId(), questionForm.getId());
  }

  @Test(expected = AnswerFormNotFilledOutException.class)
  public void createAnswerFormToUpdate_withNoAnswerFormFilledOutByUser_shouldThrowAnswerFormNotFilledOutException() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, AnswerFormNotFilledOutException {
    CreateAnswerFormDTO createAnswerFormDTO = (CreateAnswerFormDTO) beanFactory.getBean("createAnswerFormDTO");
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionFormWithAnswerForm");
    long appUserId = 1L;

    Mockito.when(questionFormService.findByIdForAnswerForm(questionForm.getId())).thenReturn(questionForm);

    CreateAnswerFormDTO createAnswerFormDTOReturned = answerFormService.createAnswerFormToUpdate(questionForm.getId(), appUserId);

    assertEquals(createAnswerFormDTOReturned.getAppUserId(), createAnswerFormDTO.getAppUserId());
    assertEquals(createAnswerFormDTOReturned.getQuestionFormId(), questionForm.getId());
  }

  @Test
  public void sortAnswerByQuestions_withValidData() {
    List<Answer> answers = (List<Answer>) beanFactory.getBean("getAnswers");
    List<Question> questions = (List<Question>) beanFactory.getBean("getListQuestions");
    answers.get(0).setQuestion(questions.get(0));

    List<Answer> returnedAnswers = answerFormService.sortAnswersByQuestions(questions, answers);

    assertEquals(1, returnedAnswers.size());
    assertEquals(answers.get(0).getActualAnswerTexts().get(0).getAnswerText(), returnedAnswers.get(0).getActualAnswerTexts().get(0).getAnswerText());
  }

  @Test
  public void findAnswerFormByAnswerId_withValidData_returnsDisplaAnswersFromAnAnswerFormDTO() throws NoSuchAnswerformById, AnswerNotFoundByIdException, BelongToAnotherUserException {
    long answerId = 1L;
    AnswerForm answerForm = (AnswerForm) beanFactory.getBean("answerForm");

    Mockito.when(answerService.findById(answerId)).thenReturn(answerForm.getAnswers().get(0));
    Mockito.when(answerFormRepository.findById(answerForm.getId())).thenReturn(java.util.Optional.of(answerForm));

    DisplayAnswersFromAnAnswerFormDTO displayAnswersFromAnAnswerFormDTOReturned = answerFormService.findAnswerFormByAnswerId(answerId);

    assertEquals(displayAnswersFromAnAnswerFormDTOReturned.getAnswers().size(), answerForm.getAnswers().size());
  }

  @Test(expected = NoSuchAnswerformById.class)
  public void findAnswerFormByAnswerId_withInvalidAnswerFormId_shouldThrowNoSuchAnswerFormById() throws NoSuchAnswerformById, AnswerNotFoundByIdException, BelongToAnotherUserException {
    long answerId = 1L;
    AnswerForm answerForm = (AnswerForm) beanFactory.getBean("answerForm");

    given(answerFormRepository.findById(answerForm.getId())).willAnswer(invocation -> {
              throw new NoSuchAnswerformById("Couldn't find answerForm with the given id");
            }
    );
    Mockito.when(answerFormRepository.findById(answerForm.getId())).thenReturn(java.util.Optional.of(answerForm));

    answerFormService.findAnswerFormByAnswerId(answerId);
  }

  @Test(expected = AnswerNotFoundByIdException.class)
  public void findAnswerFormByAnswerId_withAnswerId_shouldThrowAnswerNotFoundByIdException() throws NoSuchAnswerformById, AnswerNotFoundByIdException, BelongToAnotherUserException {
    long answerId = 1L;

    given(answerService.findById(answerId)).willAnswer(invocation -> {
              throw new AnswerNotFoundByIdException("Couldn't find answer with the given id");
            }
    );

    answerFormService.findAnswerFormByAnswerId(answerId);
  }

  @Test
  public void findAllAnswersBelongingToQuestionForm_withValidData() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, QuestionTypesAndQuestionTextsSizeMissMatchException, AnswerFormNotFilledOutException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionFormWithAnswerFormWithTextQuestion");
    long questionFormId = questionForm.getId();
    long appUserId = questionForm.getAppUser().getId();

    Mockito.when(questionFormService.findById(questionFormId)).thenReturn(questionForm);

    DisplayAllUserAnswersDTO displayAllUserAnswersDTOReturned = answerFormService.findAllAnswersBelongingToQuestionForm(questionFormId, appUserId);
    String expected = "actualAnswerText0(0.0);; actualAnswerText1(0.0);; actualAnswerText2(0.0);; actualAnswerText3(0.0);; actualAnswerText4(0.0)";

    assertEquals(displayAllUserAnswersDTOReturned.getAnswers().size(), questionForm.getQuestions().size());
    assertEquals(displayAllUserAnswersDTOReturned.getAnswers().get(0).get(0), expected);
  }

  @Test(expected = AnswerFormNotFilledOutException.class)
  public void findAllAnswersBelongingToQuestionForm_withNoAnswerToTheGivenAnswerForm_shouldThrowNoUserFilledOutAnswerFormException() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, QuestionTypesAndQuestionTextsSizeMissMatchException, AnswerFormNotFilledOutException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    AppUser appUser = new AppUser();
    questionForm.setAppUser(appUser);
    long questionFormId = questionForm.getId();
    long appUserId = appUser.getId();

    Mockito.when(questionFormService.findById(questionFormId)).thenReturn(questionForm);

    answerFormService.findAllAnswersBelongingToQuestionForm(questionFormId, appUserId);
  }

  @Test
  public void findAllAnswersBelongingToAnUser_withValidData() throws QuestionFormNotFoundException, BelongToAnotherUserException, NoSuchAnswerformById, AnswerFormNotFilledOutException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionFormWithAnswerFormWithTextQuestion");
    AppUser appUser = new AppUser();
    questionForm.setAppUser(appUser);
    long questionFormId = questionForm.getId();

    Mockito.when(questionFormService.findByIdForAnswerForm(questionFormId)).thenReturn(questionForm);
    Mockito.when(appUserService.findCurrentlyLoggedInUser()).thenReturn(appUser);

    answerFormService.findAllAnswersBelongingToAnUser(questionFormId);
  }

  @Test(expected = AnswerFormNotFilledOutException.class)
  public void findAllAnswersBelongingToAnUser_withNoUserAnswer_shouldThrowAnswerFormNotFilledOutException() throws QuestionFormNotFoundException, BelongToAnotherUserException, NoSuchAnswerformById, AnswerFormNotFilledOutException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    AppUser appUser = new AppUser();
    questionForm.setAppUser(appUser);
    long questionFormId = questionForm.getId();


    Mockito.when(questionFormService.findByIdForAnswerForm(questionFormId)).thenReturn(questionForm);
    Mockito.when(appUserService.findCurrentlyLoggedInUser()).thenReturn(appUser);

    answerFormService.findAllAnswersBelongingToAnUser(questionFormId);
  }
}
