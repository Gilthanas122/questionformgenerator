package com.bottomupquestionphd.demo.unittests.questions;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.*;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionCreateDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.question.*;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormIsNullException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.QuestionRepository;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import com.bottomupquestionphd.demo.services.deleteservice.DeleteService;
import com.bottomupquestionphd.demo.services.questions.QuestionConversionService;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import com.bottomupquestionphd.demo.services.questions.QuestionService;
import com.bottomupquestionphd.demo.services.questions.QuestionServiceImpl;
import com.bottomupquestionphd.demo.services.questiontextpossibilities.QuestionTextPossibilityService;
import com.bottomupquestionphd.demo.testconfiguration.TestConfigurationBeanFactory;
import org.hibernate.TypeMismatchException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
public class QuestionServiceTest {

  private final QuestionRepository questionRepository = Mockito.mock(QuestionRepository.class);
  private final QuestionFormService questionFormService = Mockito.mock(QuestionFormService.class);
  private final QuestionTextPossibilityService questionTextPossibilityService = Mockito.mock(QuestionTextPossibilityService.class);
  private final AppUserService appUserService = Mockito.mock(AppUserService.class);
  private final QuestionConversionService questionConversionService = Mockito.mock(QuestionConversionService.class);
  private QuestionService questionService;
  private DeleteService deleteService = Mockito.mock(DeleteService.class);
  @Autowired
  private BeanFactory beanFactory;

  @Before
  public void setup() {
    questionService = new QuestionServiceImpl(questionRepository, questionFormService, questionTextPossibilityService, appUserService, questionConversionService, deleteService);
  }

  @Test
  public void saveQuestion_withValidTextQuestion() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException, InvalidInputFormatException {
    String type = "text";
    long questionId = 1;
    QuestionCreateDTO questionCreateDTO = (QuestionCreateDTO) beanFactory.getBean("questionCreateDTO");
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    Mockito.when(questionFormService.findById(questionId)).thenReturn(questionForm);

    questionService.saveQuestion(type, questionCreateDTO, questionId);
    Mockito.verify(questionRepository, times(1)).save(any(TextQuestion.class));
  }

  @Test
  public void saveQuestion_withValidScaleQuestion() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException, InvalidInputFormatException {
    String type = "scale";
    long questionId = 1;
    QuestionCreateDTO questionCreateDTO = (QuestionCreateDTO) beanFactory.getBean("questionCreateDTO");
    questionCreateDTO.getAnswers().add("5");
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    Mockito.when(questionFormService.findById(questionId)).thenReturn(questionForm);

    questionService.saveQuestion(type, questionCreateDTO, questionId);
    Mockito.verify(questionRepository, times(1)).save(any(ScaleQuestion.class));
  }

  @Test
  public void saveQuestion_withValidCheckBoxQuestion() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException, InvalidInputFormatException {
    String type = "checkbox";
    long questionId = 1;
    QuestionCreateDTO questionCreateDTO = (QuestionCreateDTO) beanFactory.getBean("questionCreateDTO");
    questionCreateDTO.getAnswers().add("hello there");
    questionCreateDTO.getAnswers().add("hello here");
    questionCreateDTO.getAnswers().add("hello ere");
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    Mockito.when(questionFormService.findById(questionId)).thenReturn(questionForm);

    questionService.saveQuestion(type, questionCreateDTO, questionId);
    Mockito.verify(questionRepository, times(1)).save(any(CheckBoxQuestion.class));
  }

  @Test
  public void saveQuestion_withValidRadioButtonQuestion() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException, InvalidInputFormatException {
    String type = "radio";
    long questionId = 1;
    QuestionCreateDTO questionCreateDTO = (QuestionCreateDTO) beanFactory.getBean("questionCreateDTO");
    questionCreateDTO.getAnswers().add("hello there");
    questionCreateDTO.getAnswers().add("hello here");
    questionCreateDTO.getAnswers().add("hello ere");
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    Mockito.when(questionFormService.findById(questionId)).thenReturn(questionForm);

    questionService.saveQuestion(type, questionCreateDTO, questionId);
    Mockito.verify(questionRepository, times(1)).save(any(RadioButtonQuestion.class));
  }

  @Test(expected = InvalidInputFormatException.class)
  public void saveQuestion_withInValidScaleQuestionValue_ThrowsInvalidInputFormatException() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException, InvalidInputFormatException {
    String type = "scale";
    long questionId = 1;
    QuestionCreateDTO questionCreateDTO = (QuestionCreateDTO) beanFactory.getBean("questionCreateDTO");
    questionCreateDTO.getAnswers().add("null");
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    Mockito.when(questionFormService.findById(questionId)).thenReturn(questionForm);

    questionService.saveQuestion(type, questionCreateDTO, questionId);
    Mockito.verify(questionRepository, times(1)).save(any(ScaleQuestion.class));
  }

  @Test(expected = InvalidInputFormatException.class)
  public void saveQuestion_InvalidQuestionTypeFromUrlPath_ThrowsInvalidInputFormatException() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException, InvalidInputFormatException {
    String type = "checkBoxfff";
    long questionId = 1;
    QuestionCreateDTO questionCreateDTO = (QuestionCreateDTO) beanFactory.getBean("questionCreateDTO");
    questionCreateDTO.getAnswers().add(null);
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    Mockito.when(questionFormService.findById(questionId)).thenReturn(questionForm);

    questionService.saveQuestion(type, questionCreateDTO, questionId);
    Mockito.verify(questionRepository, times(1)).save(any(ScaleQuestion.class));
  }

  @Test(expected = MissingParamsException.class)
  public void saveQuestion_TooFewQuestionAnswerPossibilitiesByCheckBoxQuestion_ThrowsInvalidInputFormatException() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException, InvalidInputFormatException {
    String type = "checkbox";
    long questionId = 1;
    QuestionCreateDTO questionCreateDTO = (QuestionCreateDTO) beanFactory.getBean("questionCreateDTO");
    questionCreateDTO.getAnswers().add("null");
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    Mockito.when(questionFormService.findById(questionId)).thenReturn(questionForm);

    questionService.saveQuestion(type, questionCreateDTO, questionId);
    Mockito.verify(questionRepository, times(1)).save(any(ScaleQuestion.class));
  }

  @Test(expected = MissingParamsException.class)
  public void saveQuestion_TooFewQuestionAnswerPossibilitiesByRadioButtonQuestion_ThrowsInvalidInputFormatException() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException, InvalidInputFormatException {
    String type = "checkbox";
    long questionId = 1;
    QuestionCreateDTO questionCreateDTO = (QuestionCreateDTO) beanFactory.getBean("questionCreateDTO");
    questionCreateDTO.getAnswers().add("null");
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    Mockito.when(questionFormService.findById(questionId)).thenReturn(questionForm);

    questionService.saveQuestion(type, questionCreateDTO, questionId);
    Mockito.verify(questionRepository, times(1)).save(any(ScaleQuestion.class));
  }

  @Test(expected = NullPointerException.class)
  public void saveQuestion_NullCheckBoxQuestion_ThrowsMissingParamsException() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException, InvalidInputFormatException {
    String type = "checkbox";
    long questionId = 1;
    QuestionCreateDTO questionCreateDTO = null;
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    List<QuestionTextPossibility> questionTextPossibilities = (List<QuestionTextPossibility>) beanFactory.getBean("questionTextPossibilitiesForCheckBox");
    Mockito.when(questionFormService.findById(questionId)).thenReturn(questionForm);
    Mockito.when(questionTextPossibilityService.convertStringToQuestionTextPossibility(anyList())).thenReturn(questionTextPossibilities);

    questionService.saveQuestion(type, questionCreateDTO, questionId);
    Mockito.verify(questionRepository, times(1)).save(any(CheckBoxQuestion.class));
  }

  @Test
  public void findByIdConvertToQuestionWithDtypeDTO_withValidQuestionId_returnsQuestionWithYDTypeDTO() throws BelongToAnotherUserException, QuestionNotFoundByIdException, QuestionHasBeenAnsweredException {
    long questionId = 1;
    Mockito.when(questionRepository.existsById(questionId)).thenReturn(true);
    Mockito.when(questionRepository.findById(questionId)).thenReturn((Question) beanFactory.getBean("validQuestion"));
    Mockito.when(questionConversionService.convertFromQuestionToQuestionWithDType(any())).thenReturn((QuestionWithDTypeDTO) beanFactory.getBean("textQuestionWithDTypeDTO"));

    QuestionWithDTypeDTO question = questionService.findByIdAndConvertToQuestionWithDTypeDTO(1);

    assertEquals("TextQuestion", question.getQuestionType());
    assertEquals("TextQuestionText", question.getQuestionText());
    Mockito.verify(questionConversionService, times(1)).convertFromQuestionToQuestionWithDType(any());
  }

  @Test(expected = QuestionNotFoundByIdException.class)
  public void findByIdConvertToQuestionWithDtypeDTO_withInValidQuestionId_throwsQuestionNotFoundException() throws BelongToAnotherUserException, QuestionNotFoundByIdException, QuestionHasBeenAnsweredException {
    Mockito.when(questionRepository.existsById(1l)).thenReturn(false);
    questionService.findByIdAndConvertToQuestionWithDTypeDTO(1);
  }


  @Test
  public void findById_withValidQuestionId_returnsQuestion() throws BelongToAnotherUserException, QuestionNotFoundByIdException {
    Mockito.when(questionRepository.existsById(1l)).thenReturn(true);
    Mockito.when(questionRepository.findById(1L)).thenReturn((Question) beanFactory.getBean("validQuestion"));

    Question question = questionService.findById(1l);

    assertEquals("fakeQuestionText", question.getQuestionText());
    assertEquals(Optional.of(0), Optional.of(question.getListPosition()));
  }

  @Test(expected = QuestionNotFoundByIdException.class)
  public void findById_withInValidQuestionId_throwsQuestionNotFoundException() throws BelongToAnotherUserException, QuestionNotFoundByIdException {
    Mockito.when(questionRepository.existsById(1l)).thenReturn(false);
    questionService.findById(1l);
  }

  @Test
  public void saveQuestionFromQuestionDType_withValidQuestionWithDTypeCheckBoxQuestion() throws QuestionNotFoundByIdException, MissingParamsException, BelongToAnotherUserException {
    QuestionWithDTypeDTO questionWithDTypeDTO = (QuestionWithDTypeDTO) beanFactory.getBean("checkboxQuestionWithDTypeDTO");
    CheckBoxQuestion checkBoxQuestion = (CheckBoxQuestion) beanFactory.getBean("checkboxQuestion");
    AppUser appUser = new AppUser.Builder().build();
    checkBoxQuestion.getQuestionForm().setAppUser(appUser);

    Mockito.when(questionConversionService.convertQuestionWithDTypeToQuestion(questionWithDTypeDTO)).thenReturn((CheckBoxQuestion) beanFactory.getBean("checkboxQuestion"));
    Mockito.when(questionRepository.existsById(questionWithDTypeDTO.getId())).thenReturn(true);
    Mockito.when(questionRepository.findById(questionWithDTypeDTO.getId())).thenReturn(checkBoxQuestion);

    doNothing().when(appUserService).checkIfCurrentUserMatchesUserIdInPath(anyLong());
    questionService.saveQuestionFromQuestionDType(questionWithDTypeDTO);
    Mockito.verify(questionRepository, times(1)).save(any());

  }

  @Test(expected = TypeMismatchException.class)
  public void saveQuestionFromQuestionDType_withNotMatchingQuestionTypesBetweenDBAndUserInput_throwsTypeMissMatchException() throws QuestionNotFoundByIdException, MissingParamsException, BelongToAnotherUserException {
    QuestionWithDTypeDTO questionWithDTypeDTO = (QuestionWithDTypeDTO) beanFactory.getBean("checkboxQuestionWithDTypeDTO");
    CheckBoxQuestion checkBoxQuestion = (CheckBoxQuestion) beanFactory.getBean("checkboxQuestion");
    AppUser appUser = new AppUser.Builder().build();
    checkBoxQuestion.getQuestionForm().setAppUser(appUser);

    Mockito.when(questionConversionService.convertQuestionWithDTypeToQuestion(questionWithDTypeDTO)).thenReturn((RadioButtonQuestion) beanFactory.getBean("radioButtonQuestion"));
    Mockito.when(questionRepository.existsById(questionWithDTypeDTO.getId())).thenReturn(true);
    Mockito.when(questionRepository.findById(questionWithDTypeDTO.getId())).thenReturn(checkBoxQuestion);

    doNothing().when(appUserService).checkIfCurrentUserMatchesUserIdInPath(anyLong());

    questionService.saveQuestionFromQuestionDType(questionWithDTypeDTO);
    Mockito.verify(questionRepository, times(1)).save(any());

  }

  @Test
  public void changeOrderOfQuestion_withValidChangeAndQuestionId() throws InvalidQuestionPositionException, InvalidQuestionPositionChangeException, QuestionNotFoundByIdException, BelongToAnotherUserException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    Question question = questionForm.getQuestions().get(0);
    question.setQuestionForm(questionForm);
    Question question1ToBeSwitchedWith = questionForm.getQuestions().get(1);
    question1ToBeSwitchedWith.setQuestionForm(questionForm);
    questionForm.setAppUser(new AppUser.Builder().build());

    Mockito.when(questionRepository.existsById(question.getId())).thenReturn(true);
    Mockito.when(questionRepository.findById(question.getId())).thenReturn(question);
    Mockito.when(questionFormService.findQuestionToSwitchPositionWith(questionForm, question.getListPosition(), "down")).thenReturn(question1ToBeSwitchedWith);
    doNothing().when(appUserService).checkIfCurrentUserMatchesUserIdInPath(anyLong());

    questionService.changeOrderOfQuestion("down", question.getId());
    assertEquals(Optional.of(1), Optional.of(question.getListPosition()));
    assertEquals(Optional.of(0), Optional.of(question1ToBeSwitchedWith.getListPosition()));
  }

  @Test(expected = InvalidQuestionPositionChangeException.class)
  public void changeOrderOfQuestion_withInValidChangeValue_throwsInvalidQuestionPositionChangeException() throws InvalidQuestionPositionException, InvalidQuestionPositionChangeException, QuestionNotFoundByIdException, BelongToAnotherUserException {
    Question question = (Question) beanFactory.getBean("validQuestion");
    question.getQuestionForm().setAppUser(new AppUser.Builder().build());

    Mockito.when(questionRepository.existsById(question.getId())).thenReturn(true);
    Mockito.when(questionRepository.findById(question.getId())).thenReturn(question);
    doNothing().when(appUserService).checkIfCurrentUserMatchesUserIdInPath(anyLong());

    questionService.changeOrderOfQuestion("kaki", 1L);
  }

  @Test(expected = InvalidQuestionPositionException.class)
  public void changeOrderOfQuestion_withInvalidPositionLast_throwsInvalidQuestionPositionException() throws InvalidQuestionPositionException, InvalidQuestionPositionChangeException, QuestionNotFoundByIdException, BelongToAnotherUserException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    Question question = questionForm.getQuestions().get(3);
    question.setQuestionForm(questionForm);
    questionForm.setAppUser(new AppUser.Builder().build());

    Mockito.when(questionRepository.existsById(question.getId())).thenReturn(true);
    Mockito.when(questionRepository.findById(question.getId())).thenReturn(question);
    doNothing().when(appUserService).checkIfCurrentUserMatchesUserIdInPath(anyLong());

    questionService.changeOrderOfQuestion("down", question.getId());
  }

  @Test(expected = InvalidQuestionPositionException.class)
  public void changeOrderOfQuestion_withInvalidPositionFirst_throwsInvalidQuestionPositionException() throws InvalidQuestionPositionException, InvalidQuestionPositionChangeException, QuestionNotFoundByIdException, BelongToAnotherUserException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");
    Question question = questionForm.getQuestions().get(0);
    question.setQuestionForm(questionForm);
    questionForm.setAppUser(new AppUser.Builder().build());

    Mockito.when(questionRepository.existsById(question.getId())).thenReturn(true);
    Mockito.when(questionRepository.findById(question.getId())).thenReturn(question);
    doNothing().when(appUserService).checkIfCurrentUserMatchesUserIdInPath(anyLong());

    questionService.changeOrderOfQuestion("up", question.getId());
  }

  @Test
  public void deleteQuestion_withValidQuestionId_returnsQuestionFormId() throws QuestionNotFoundByIdException, BelongToAnotherUserException, QuestionFormIsNullException, QuestionHasBeenAnsweredException {
    Question question = (Question) beanFactory.getBean("validQuestion");
    question.getQuestionForm().setId(3);

    Mockito.when(questionRepository.existsById(question.getId())).thenReturn(true);
    Mockito.when(questionRepository.findById(question.getId())).thenReturn(question);
    long questionFormId = questionService.deleteQuestion(question.getId());

    assertEquals(3, questionFormId);
    Mockito.verify(questionRepository, times(1)).setToBeDeleted(question.getId());
  }

  @Test
  public void findQuestionFormIdBelongingToQuestion_withValidQuestionId_returnQuestionFormId() throws QuestionNotFoundByIdException {
    Question question = (Question) beanFactory.getBean("validQuestion");
    question.getQuestionForm().setId(3);

    Mockito.when(questionRepository.existsById(question.getId())).thenReturn(true);
    Mockito.when(questionRepository.findById(question.getId())).thenReturn(question);
    long questionFormId = questionService.findQuestionFormIdBelongingToQuestion(question.getId());

    Assert.assertEquals(3, questionFormId);
    Mockito.verify(questionRepository, times(1)).findById(question.getId());
  }
}
