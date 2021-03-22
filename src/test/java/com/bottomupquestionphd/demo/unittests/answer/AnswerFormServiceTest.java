package com.bottomupquestionphd.demo.unittests.answer;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.answerform.CreateAnswerFormDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.answerform.AnswerFormAlreadyFilledOutByCurrentUserException;
import com.bottomupquestionphd.demo.exceptions.answerform.NoSuchAnswerformById;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.AnswerFormRepository;
import com.bottomupquestionphd.demo.services.answerforms.AnswerFormService;
import com.bottomupquestionphd.demo.services.answerforms.AnswerFormServiceImpl;
import com.bottomupquestionphd.demo.services.answers.AnswerService;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
public class AnswerFormServiceTest {
  private final AnswerFormRepository answerFormRepository = Mockito.mock(AnswerFormRepository.class);
  private final QuestionFormService questionFormService = Mockito.mock(QuestionFormService.class);
  private final AppUserService appUserService = Mockito.mock(AppUserService.class);
  private final AnswerService answerService = Mockito.mock(AnswerService.class);

  @Autowired
  private BeanFactory beanFactory;

  private AnswerFormService answerFormService;

  @Before
  public void setup() {
    answerFormService = new AnswerFormServiceImpl(answerFormRepository, questionFormService, appUserService, answerService);
  }

  @Test
  public void createAnswerForm_withValidInput() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, AnswerFormAlreadyFilledOutByCurrentUserException {

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

  @Test
  public void saveAnswerForm_withValidInputData() throws QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException, MissingUserException, NoSuchUserByIdException, AnswerFormAlreadyFilledOutByCurrentUserException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    AnswerForm answerForm = (AnswerForm) beanFactory.getBean("answerForm");
    long answerFormId = 1;
    long questionFormId = 1;
    long appUserId = 1;
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");

    Mockito.when(appUserService.findById(appUserId)).thenReturn(appUser);
    Mockito.when(questionFormService.findByIdForAnswerForm(questionFormId)).thenReturn(questionForm);
    answerFormService.saveAnswerForm(answerForm, answerFormId, questionFormId, appUserId);

    Mockito.verify(answerFormRepository, times(1)).save(answerForm);
    Assert.assertEquals(answerForm.getAnswers().get(0).getAnswerForm(), answerForm);
  }

  @Test(expected = MissingParamsException.class)
  public void saveAnswerForm_withNullInput_throwsMissingParamsException() throws QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException, MissingUserException, NoSuchUserByIdException, AnswerFormAlreadyFilledOutByCurrentUserException {
    AnswerForm answerForm = null;
    long answerFormId = 1;
    long questionFormId = 1;
    long appUserId = 1;
    answerFormService.saveAnswerForm(answerForm, answerFormId, questionFormId, appUserId);
  }

  @Test(expected = MissingParamsException.class)
  public void saveAnswerForm_withEmptyAnswerText_throwsMissingParamsException() throws QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException, MissingUserException, NoSuchUserByIdException, AnswerFormAlreadyFilledOutByCurrentUserException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    AnswerForm answerForm = (AnswerForm) beanFactory.getBean("answerForm");
    answerForm.getAnswers().get(0).getActualAnswerTexts().get(0).setAnswerText(null);
    long answerFormId = 1;
    long questionFormId = 1;
    long appUserId = 1;
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");

    Mockito.when(appUserService.findById(appUserId)).thenReturn(appUser);
    Mockito.when(questionFormService.findByIdForAnswerForm(questionFormId)).thenReturn(questionForm);
    answerFormService.saveAnswerForm(answerForm, answerFormId, questionFormId, appUserId);

    Mockito.verify(answerFormRepository, times(1)).save(answerForm);
    Assert.assertEquals(answerForm.getAnswers().get(0).getAnswerForm(), answerForm);
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

    given(answerFormRepository.findById(answerFormId)).willAnswer(
            invocation ->
            {
              throw new NoSuchAnswerformById("Ooops");
            }
    );

    answerFormService.findAnswerFormById(answerFormId);
  }

  @Test
  public void saveUpdatedAnswerForm_withValidInput() throws BelongToAnotherUserException, NoSuchUserByIdException, MissingParamsException, NoSuchAnswerformById {
    AnswerForm answerForm = (AnswerForm) beanFactory.getBean("answerForm");
    answerForm.getAnswers().get(0).getActualAnswerTexts().get(0).setAnswerText(null);
    long answerFormId = answerForm.getId();
    long appUserId = answerForm.getAppUser().getId();

    Mockito.when(answerFormRepository.findById(answerFormId)).thenReturn(java.util.Optional.of(answerForm));

    answerFormService.saveUpdatedAnswerForm(answerFormId, appUserId, answerForm);

    Mockito.verify(answerFormRepository, times(1)).save(answerForm);
  }

  @Test(expected = BelongToAnotherUserException.class)
  public void saveUpdatedAnswerForm_withNotMatchingAppUserId_throwsBelongsToAnotherUserException() throws BelongToAnotherUserException, NoSuchUserByIdException, MissingParamsException, NoSuchAnswerformById {
    long appUserId = 12;
    AnswerForm answerForm = (AnswerForm) beanFactory.getBean("answerForm");
    long answerFormId = answerForm.getId();

    Mockito.when(answerFormRepository.findById(answerFormId)).thenReturn(java.util.Optional.of(answerForm));

    answerFormService.saveUpdatedAnswerForm(answerFormId, appUserId, answerForm);
  }
}
