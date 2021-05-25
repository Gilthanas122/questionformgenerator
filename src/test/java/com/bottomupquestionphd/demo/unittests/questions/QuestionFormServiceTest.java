package com.bottomupquestionphd.demo.unittests.questions;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.CheckBoxQuestion;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.bottomupquestionphd.demo.domains.daos.questions.TextQuestion;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormCreateDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserNameException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionChangeException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionException;
import com.bottomupquestionphd.demo.exceptions.questionform.*;
import com.bottomupquestionphd.demo.repositories.QuestionFormRepository;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import com.bottomupquestionphd.demo.services.namedparameterservice.QueryService;
import com.bottomupquestionphd.demo.services.questions.QuestionConversionService;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import com.bottomupquestionphd.demo.services.questions.QuestionFormServiceImpl;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
public class QuestionFormServiceTest {

  private final QuestionFormRepository questionFormRepository = Mockito.mock(QuestionFormRepository.class);
  private final AppUserService appUserService = Mockito.mock(AppUserService.class);
  private final QuestionConversionService questionConversionService = Mockito.mock(QuestionConversionService.class);
  private final QueryService queryService = Mockito.mock(QueryService.class);

  private QuestionFormService questionFormService;

  @Autowired
  private BeanFactory beanFactory;

  @Before
  public void setup() {
    questionFormService = new QuestionFormServiceImpl(questionFormRepository, appUserService, questionConversionService, queryService);
  }

  @Test
  public void save_withValidQuestionForm_returnsQuestionFormId() throws NoSuchUserNameException, QuestionFormNameAlreadyExistsException, MissingParamsException {
    QuestionFormCreateDTO questionFormCreateDTO = (QuestionFormCreateDTO) beanFactory.getBean("questionFormCreateDTO");
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");

    Mockito.when(questionFormRepository.existsByName(questionFormCreateDTO.getName())).thenReturn(false);
    Mockito.when(appUserService.findCurrentlyLoggedInUser()).thenReturn(appUser);

    long questionFormId = questionFormService.save(questionFormCreateDTO);

    Assert.assertEquals(0, questionFormId);
    Mockito.verify(questionFormRepository, times(1)).existsByName(questionFormCreateDTO.getName());
  }

  @Test(expected = NullPointerException.class)
  public void save_withNullQuestionForm_throwsNullPointerException() throws NoSuchUserNameException, QuestionFormNameAlreadyExistsException, MissingParamsException {
    QuestionFormCreateDTO questionFormCreateDTO = null;

    questionFormService.save(questionFormCreateDTO);
  }

  @Test(expected = QuestionFormNameAlreadyExistsException.class)
  public void save_withQuestionFormsNameAlreadyTaken_throwsQuestionFormNameAlreadyExistsException() throws NoSuchUserNameException, QuestionFormNameAlreadyExistsException, MissingParamsException {
    QuestionFormCreateDTO questionFormCreateDTO = (QuestionFormCreateDTO) beanFactory.getBean("questionFormCreateDTO");

    Mockito.when(questionFormRepository.existsByName(questionFormCreateDTO.getName())).thenReturn(true);

    questionFormService.save(questionFormCreateDTO);
  }

  @Test
  public void findById_withValidId_returnsQuestionForm() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    questionForm.setId(3);
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    questionForm.setAppUser(appUser);

    Mockito.when(questionFormRepository.existsById(questionForm.getId())).thenReturn(true);
    Mockito.when(questionFormRepository.findById(questionForm.getId())).thenReturn(questionForm);

    QuestionForm questionFormReturned = questionFormService.findById(questionForm.getId());

    Assert.assertEquals(3, questionFormReturned.getId());
    Assert.assertEquals("question form name", questionFormReturned.getName());

    Mockito.verify(questionFormRepository, times(1)).findById(questionForm.getId());
  }

  @Test(expected = MissingUserException.class)
  public void findById_withNullAppUser_throwsMissingUserException() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");

    Mockito.when(questionFormRepository.existsById(questionForm.getId())).thenReturn(true);
    Mockito.when(questionFormRepository.findById(questionForm.getId())).thenReturn(questionForm);

    questionFormService.findById(questionForm.getId());
  }

  @Test(expected = QuestionFormNotFoundException.class)
  public void findById_withInValidId_returnsQuestionForm_throwsQuestionFormNotFoundException() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    questionForm.setId(3);

    Mockito.when(questionFormRepository.existsById(questionForm.getId())).thenReturn(false);
    Mockito.when(questionFormRepository.findById(questionForm.getId())).thenReturn(questionForm);

    questionFormService.findById(questionForm.getId());
  }


  @Test
  public void findAll_withQuestionFormsInDBByTheUser_returnsListOfQuestionForms() throws NoQuestionFormsInDatabaseException {
    List<QuestionForm> questionForms = (List<QuestionForm>) beanFactory.getBean("questionForms");
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setId(3);

    Mockito.when(appUserService.findCurrentlyLoggedInUser()).thenReturn(appUser);
    Mockito.when(questionFormRepository.findAllByAppUserId(appUser.getId())).thenReturn(questionForms);

    List<QuestionForm> returnedQuestionForms = questionFormService.findAll();

    Assert.assertEquals(4, returnedQuestionForms.size());
    Assert.assertEquals("question form name0", returnedQuestionForms.get(0).getName());

    Mockito.verify(questionFormRepository, times(1)).findAllByAppUserId(appUser.getId());
  }

  @Test(expected = NoQuestionFormsInDatabaseException.class)
  public void findAll_withNoQuestionFormsInDBByTheUser_throwsNoQuestionFormInDatabaseException() throws NoQuestionFormsInDatabaseException {
    List<QuestionForm> questionForms = new ArrayList<>();
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setId(3);

    Mockito.when(appUserService.findCurrentlyLoggedInUser()).thenReturn(appUser);
    Mockito.when(questionFormRepository.findAllByAppUserId(appUser.getId())).thenReturn(questionForms);

    questionFormService.findAll();
    Mockito.verify(questionFormRepository, times(1)).findAllByAppUserId(appUser.getId());
  }

  @Test
  public void finishQuestionForm_withEnoughQuestions() throws BelongToAnotherUserException, QuestionFormNotFoundException, NotEnoughQuestionsToCreateFormException, MissingUserException {
    QuestionForm questionForm = (QuestionForm)  beanFactory.getBean("questionForm");
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    questionForm.setAppUser(appUser);
    questionForm.setId(3);

    Mockito.when(questionFormRepository.existsById(questionForm.getId())).thenReturn(true);
    Mockito.when(questionFormRepository.findById(questionForm.getId())).thenReturn(questionForm);

    questionFormService.finishQuestionForm(questionForm.getId());

    Mockito.verify(questionFormRepository, times(1)).save(questionForm);
    Assert.assertTrue(questionForm.isFinished());
  }

  @Test(expected = NotEnoughQuestionsToCreateFormException.class)
  public void finishQuestionForm_withTooFewQuestions_throwsNotEnoughQuestionsToCreateAFormException() throws BelongToAnotherUserException, QuestionFormNotFoundException, NotEnoughQuestionsToCreateFormException, MissingUserException {
    QuestionForm questionForm = (QuestionForm)  beanFactory.getBean("questionForm");
    List<Question> questions = new ArrayList<>();
    questionForm.setQuestions(questions);
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    questionForm.setAppUser(appUser);
    questionForm.setId(3);

    Mockito.when(questionFormRepository.existsById(questionForm.getId())).thenReturn(true);
    Mockito.when(questionFormRepository.findById(questionForm.getId())).thenReturn(questionForm);

    questionFormService.finishQuestionForm(questionForm.getId());
  }

  @Test
  public void findByIdAndAddQuestionType_withValidQuestionId_returnQuestionWithDTypeDTO() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
    QuestionWithDTypeDTO questionWithDTypeDTO = (QuestionWithDTypeDTO) beanFactory.getBean("textQuestionWithDTypeDTO");
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    questionForm.setId(3);
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    questionForm.setAppUser(appUser);
    Question question = (Question) beanFactory.getBean("validQuestion");

    Mockito.when(questionFormRepository.existsById(questionForm.getId())).thenReturn(true);
    Mockito.when(questionFormRepository.findById(questionForm.getId())).thenReturn(questionForm);
    Mockito.when(questionConversionService.convertFromQuestionToQuestionWithDType(question)).thenReturn(questionWithDTypeDTO);

    List<QuestionWithDTypeDTO> returnedQuestionWithDTypeDTOs = questionFormService.findByIdAndAddQuestionType(3);

    Assert.assertEquals(questionForm.getQuestions().size(), returnedQuestionWithDTypeDTOs.size());
  }

  @Test
  public void findQuestionToSwitchPositionWith_withValidInputUp_returnsQuestion() throws InvalidQuestionPositionException, InvalidQuestionPositionChangeException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    String direction = "up";
    int currentListPosition =1;

    Question question = questionFormService.findQuestionToSwitchPositionWith(questionForm, currentListPosition, direction);

    Assert.assertEquals(Optional.of(0), Optional.of(question.getListPosition()));
  }

  @Test
  public void findQuestionToSwitchPositionWith_withValidInputDown_returnsQuestion() throws InvalidQuestionPositionException, InvalidQuestionPositionChangeException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    String direction = "down";
    int currentListPosition =1;

    Question question = questionFormService.findQuestionToSwitchPositionWith(questionForm, currentListPosition, direction);

    Assert.assertEquals(Optional.of(2), Optional.of(question.getListPosition()));
  }

  @Test(expected = InvalidQuestionPositionChangeException.class)
  public void findQuestionToSwitchPositionWith_withInvalidDirection_throwsInvalidQuestionPositionChangeException() throws InvalidQuestionPositionException, InvalidQuestionPositionChangeException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    String direction = "hello";
    int currentListPosition =1;

    questionFormService.findQuestionToSwitchPositionWith(questionForm, currentListPosition, direction);
  }

  @Test(expected = InvalidQuestionPositionChangeException.class)
  public void findQuestionToSwitchPositionWith_withInvalidDirectionNull_throwsInvalidQuestionPositionChangeException() throws InvalidQuestionPositionException, InvalidQuestionPositionChangeException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    String direction = null;
    int currentListPosition =1;

    questionFormService.findQuestionToSwitchPositionWith(questionForm, currentListPosition, direction);
  }

  @Test(expected = InvalidQuestionPositionException.class)
  public void findQuestionToSwitchPositionWith_withInvalidPositionForUp_throwsInvalidQuestionPositionChangeException() throws InvalidQuestionPositionException, InvalidQuestionPositionChangeException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    String direction = "up";
    int currentListPosition =0;

    questionFormService.findQuestionToSwitchPositionWith(questionForm, currentListPosition, direction);
  }

  @Test(expected = InvalidQuestionPositionException.class)
  public void findQuestionToSwitchPositionWith_withInvalidPositionForDown_throwsInvalidQuestionPositionChangeException() throws InvalidQuestionPositionException, InvalidQuestionPositionChangeException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    String direction = "down";
    int currentListPosition =3;

    questionFormService.findQuestionToSwitchPositionWith(questionForm, currentListPosition, direction);
  }

  @Test
  public void updateQuestionListPositionAfterDeletingQuestion_withValidQuestionForm() throws QuestionFormIsNullException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    questionForm.getQuestions().get(0).setDeleted(true);

    questionFormService.updateQuestionListPositionAfterDeletingQuestion(questionForm);

    Assert.assertEquals("helloka1", questionForm.getQuestions().get(0).getQuestionText());
    Assert.assertEquals("helloka2", questionForm.getQuestions().get(1).getQuestionText());
    Mockito.verify(questionFormRepository, times(1)).save(questionForm);
  }

  @Test(expected = QuestionFormIsNullException.class)
  public void updateQuestionListPositionAfterDeletingQuestion_withNullQuestionForm() throws QuestionFormIsNullException {
    QuestionForm questionForm = null;

    questionFormService.updateQuestionListPositionAfterDeletingQuestion(questionForm);
  }

  @Test
  public void findByIdForAnswerForm_withValidquestionFormId_returnQuestionForm() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");

    Mockito.when(questionFormRepository.existsById(questionForm.getId())).thenReturn(true);
    Mockito.when(questionFormRepository.findById(questionForm.getId())).thenReturn(questionForm);

    QuestionForm returnedQuestionForm = questionFormService.findByIdForAnswerForm(questionForm.getId());
    Assert.assertEquals(questionForm.getId(), returnedQuestionForm.getId());
    Mockito.verify(questionFormRepository, times(1)).findById(questionForm.getId());
  }

  @Test
  public void deleteQuestionForm_withValidQuestionFormId() throws QuestionFormNotFoundException, BelongToAnotherUserException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");

    Mockito.when(questionFormRepository.existsById(questionForm.getId())).thenReturn(true);

    questionFormService.deleteQuestionForm(questionForm.getId());

    Mockito.verify(questionFormRepository, times(1)).deleteQuestionFormById(questionForm.getId());
    Mockito.verify(queryService, times(1)).deleteQuestionsBelongingToQuestionForm(questionForm.getId());
  }

  @Test
  public void getAllTextQuestionIdsFromQuestionForm_withTwoTextQuestions_returnListLong(){
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    TextQuestion textQuestion = (TextQuestion) beanFactory.getBean("textQuestion");
    textQuestion.setId(1);
    TextQuestion textQuestion2 = (TextQuestion) beanFactory.getBean("textQuestion");
    textQuestion2.setId(2);
    CheckBoxQuestion checkBoxQuestion = (CheckBoxQuestion) beanFactory.getBean("checkboxQuestion");
    checkBoxQuestion.setId(3);
    List<Question> questions = new ArrayList<>(List.of(textQuestion, textQuestion2, checkBoxQuestion));
    questionForm.setQuestions(questions);

    List<Long> textQuestionIds = questionFormService.getAllTextQuestionIdsFromQuestionForm(questionForm);

    Assert.assertTrue(textQuestionIds.size() == 2);
    Assert.assertTrue(textQuestionIds.contains(1l));
    Assert.assertTrue(textQuestionIds.contains(2l));

  }

  @Test
  public void getAllTextQuestionIdsFromQuestionForm_withNoTextQuestions_returnListLong(){
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    CheckBoxQuestion checkBoxQuestion = (CheckBoxQuestion) beanFactory.getBean("checkboxQuestion");
    checkBoxQuestion.setId(1);
    CheckBoxQuestion checkBoxQuestion2 = (CheckBoxQuestion) beanFactory.getBean("checkboxQuestion");
    checkBoxQuestion2.setId(2);
    CheckBoxQuestion checkBoxQuestion3 = (CheckBoxQuestion) beanFactory.getBean("checkboxQuestion");
    checkBoxQuestion3.setId(3);
    List<Question> questions = new ArrayList<>(List.of(checkBoxQuestion, checkBoxQuestion2, checkBoxQuestion3));
    questionForm.setQuestions(questions);

    List<Long> textQuestionIds = questionFormService.getAllTextQuestionIdsFromQuestionForm(questionForm);

    Assert.assertTrue(textQuestionIds.isEmpty());
  }

  @Test
  public void updateQuestionForm_withValidData() throws QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException {
    QuestionFormCreateDTO questionFormCreateDTO = (QuestionFormCreateDTO) beanFactory.getBean("questionFormCreateDTO");
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    questionForm.setAppUser(appUser);

    Mockito.when(questionFormRepository.existsById(anyLong())).thenReturn(true);
    Mockito.when(questionFormRepository.findById(anyLong())).thenReturn(questionForm);
    doNothing().when(appUserService).checkIfCurrentUserMatchesUserIdInPath(anyLong());

    questionFormService.updateQuestionForm(questionFormCreateDTO, 2L);

    Mockito.verify(questionFormRepository, times(1)).save(questionForm);
  }

  @Test(expected = MissingParamsException.class)
  public void updateQuestionForm_withMissingParam_shouldThrowMissingParamsException() throws QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException {
    QuestionFormCreateDTO questionFormCreateDTO = (QuestionFormCreateDTO) beanFactory.getBean("questionFormCreateDTO");
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    questionFormCreateDTO.setDescription(null);
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    questionForm.setAppUser(appUser);

    questionFormService.updateQuestionForm(questionFormCreateDTO, 2L);
  }

  @Test(expected = QuestionFormNotFoundException.class)
  public void updateQuestionForm_withNonExistentId_shouldThrowQuestionFormNotFoundException() throws QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException {
    QuestionFormCreateDTO questionFormCreateDTO = (QuestionFormCreateDTO) beanFactory.getBean("questionFormCreateDTO");
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    questionForm.setAppUser(appUser);

    Mockito.when(questionFormRepository.existsById(anyLong())).thenReturn(false);
    questionFormService.updateQuestionForm(questionFormCreateDTO, 2L);

  }

  @Test(expected = BelongToAnotherUserException.class)
  public void updateQuestionForm_withQuestionFormIdBelongingToAnotherUser_shouldThrowBelongsToAnotherUserException() throws QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException {
    QuestionFormCreateDTO questionFormCreateDTO = (QuestionFormCreateDTO) beanFactory.getBean("questionFormCreateDTO");
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    questionForm.setAppUser(appUser);

    Mockito.when(questionFormRepository.existsById(anyLong())).thenReturn(true);
    Mockito.when(questionFormRepository.findById(anyLong())).thenReturn(questionForm);
    doThrow(BelongToAnotherUserException.class)
            .when(appUserService)
            .checkIfCurrentUserMatchesUserIdInPath(anyLong());

    questionFormService.updateQuestionForm(questionFormCreateDTO, 2L);
  }

}
