package com.bottomupquestionphd.demo.unittests.questions;

import com.bottomupquestionphd.demo.domains.daos.questions.*;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import com.bottomupquestionphd.demo.services.questions.QuestionConversionService;
import com.bottomupquestionphd.demo.services.questions.QuestionConversionServiceImpl;
import com.bottomupquestionphd.demo.testconfiguration.TestConfigurationBeanFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
public class QuestionConversionServiceTest {

  private QuestionConversionService questionConversionService;
  @Autowired
  private BeanFactory beanFactory;

  @Before
  public void setup() {
    questionConversionService = new QuestionConversionServiceImpl();
  }

  @Test
  public void convertFromQuestionToQuestionWithDType_withValidTextQuestion_returnsQuestionWithDTypeDTOWithQuestionTypeText() {
    Question question = (Question) beanFactory.getBean("textQuestion");
    QuestionWithDTypeDTO questionWithDTypeDTO = questionConversionService.convertFromQuestionToQuestionWithDType(question);

    Assert.assertEquals(questionWithDTypeDTO.getQuestionType(), QuestionType.TEXTQUESTION.toString());
    Assert.assertEquals(questionWithDTypeDTO.getQuestionText(), question.getQuestionText());
    Assert.assertEquals(questionWithDTypeDTO.getId(), question.getId());
    Assert.assertEquals(questionWithDTypeDTO.getQuestionFormId(), question.getQuestionForm().getId());
  }

  @Test
  public void convertFromQuestionToQuestionWithDType_withValidScaleQuestion_returnsQuestionWithDTypeDTOWithQuestionTypeScale() {
    Question question = (Question) beanFactory.getBean("scaleQuestion");
    QuestionWithDTypeDTO questionWithDTypeDTO = questionConversionService.convertFromQuestionToQuestionWithDType(question);

    Assert.assertEquals(questionWithDTypeDTO.getQuestionType(), QuestionType.SCALEQUESTION.toString());
    Assert.assertEquals(questionWithDTypeDTO.getQuestionText(), question.getQuestionText());
    Assert.assertEquals(questionWithDTypeDTO.getId(), question.getId());
    Assert.assertEquals(questionWithDTypeDTO.getQuestionFormId(), question.getQuestionForm().getId());
  }

  @Test
  public void convertFromQuestionToQuestionWithDType_withValidCheckBoxQuestion_returnsQuestionWithDTypeDTOWithQuestionTypeCheckBox() {
    Question question = (Question) beanFactory.getBean("checkboxQuestion");
    QuestionWithDTypeDTO questionWithDTypeDTO = questionConversionService.convertFromQuestionToQuestionWithDType(question);

    Assert.assertEquals(questionWithDTypeDTO.getQuestionType(), QuestionType.CHECKBOXQUESTION.toString());
    Assert.assertEquals(questionWithDTypeDTO.getQuestionText(), question.getQuestionText());
    Assert.assertEquals(questionWithDTypeDTO.getId(), question.getId());
    Assert.assertEquals(questionWithDTypeDTO.getQuestionFormId(), question.getQuestionForm().getId());
  }

  @Test
  public void convertFromQuestionToQuestionWithDType_withValidRadioButtonQuestion_returnsQuestionWithDTypeDTOWithQuestionTypeRadioButton() {
    Question question = (Question) beanFactory.getBean("radioButtonQuestion");
    QuestionWithDTypeDTO questionWithDTypeDTO = questionConversionService.convertFromQuestionToQuestionWithDType(question);

    Assert.assertEquals(questionWithDTypeDTO.getQuestionType(), QuestionType.RADIOBUTTONQUESTION.toString());
    Assert.assertEquals(questionWithDTypeDTO.getQuestionText(), question.getQuestionText());
    Assert.assertEquals(questionWithDTypeDTO.getId(), question.getId());
    Assert.assertEquals(questionWithDTypeDTO.getQuestionFormId(), question.getQuestionForm().getId());
  }

  @Test
  public void convertQuestionWithDTypeDTOToQuestion_withTextQuestion_returnsTextQuestion() {
    QuestionWithDTypeDTO questionWithDTypeDTO = (QuestionWithDTypeDTO) beanFactory.getBean("textQuestionWithDTypeDTO");

    Assert.assertEquals("TextQuestionText", questionConversionService.convertQuestionWithDTypeToQuestion(questionWithDTypeDTO).getQuestionText());
    Assert.assertTrue(questionConversionService.convertQuestionWithDTypeToQuestion(questionWithDTypeDTO) instanceof TextQuestion);
  }

  @Test
  public void convertQuestionWithDTypeDTOToQuestion_withScaleQuestion_returnScaleQuestion() {
    QuestionWithDTypeDTO questionWithDTypeDTO = (QuestionWithDTypeDTO) beanFactory.getBean("scaleQuestionWithDTypeDTO");

    Assert.assertEquals("ScaleQuestionText", questionConversionService.convertQuestionWithDTypeToQuestion(questionWithDTypeDTO).getQuestionText());
    Assert.assertTrue(questionConversionService.convertQuestionWithDTypeToQuestion(questionWithDTypeDTO) instanceof ScaleQuestion);
  }

  @Test
  public void convertQuestionWithDTypeDTOToQuestion_withCheckBoxQuestion_returnCheckBoxQuestion() {
    QuestionWithDTypeDTO questionWithDTypeDTO = (QuestionWithDTypeDTO) beanFactory.getBean("checkboxQuestionWithDTypeDTO");

    Assert.assertEquals("CheckboxQuestionText", questionConversionService.convertQuestionWithDTypeToQuestion(questionWithDTypeDTO).getQuestionText());
    Assert.assertTrue(questionConversionService.convertQuestionWithDTypeToQuestion(questionWithDTypeDTO) instanceof CheckBoxQuestion);
    Assert.assertEquals(5, ((CheckBoxQuestion) questionConversionService.convertQuestionWithDTypeToQuestion(questionWithDTypeDTO)).getQuestionTextPossibilities().size());
  }

  @Test
  public void convertQuestionWithDTypeDTOToQuestion_withRadioButtonQuestion_returnRadioButtonQuestion() {
    QuestionWithDTypeDTO questionWithDTypeDTO = (QuestionWithDTypeDTO) beanFactory.getBean("radioQuestionWithDTypeDTO");

    Assert.assertEquals("RadioQuestionText", questionConversionService.convertQuestionWithDTypeToQuestion(questionWithDTypeDTO).getQuestionText());
    Assert.assertTrue(questionConversionService.convertQuestionWithDTypeToQuestion(questionWithDTypeDTO) instanceof RadioButtonQuestion);
    Assert.assertEquals(5, ((RadioButtonQuestion) questionConversionService.convertQuestionWithDTypeToQuestion(questionWithDTypeDTO)).getQuestionTextPossibilities().size());
  }
}
