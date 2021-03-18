package com.bottomupquestionphd.demo.unittests.questions;

import com.bottomupquestionphd.demo.domains.daos.questions.QuestionTextPossibility;
import com.bottomupquestionphd.demo.repositories.QuestionTextPossibilityRepository;
import com.bottomupquestionphd.demo.services.questiontextpossibilities.QuestionTextPossibilityService;
import com.bottomupquestionphd.demo.services.questiontextpossibilities.QuestionTextPossibilityServiceImpl;
import com.bottomupquestionphd.demo.testconfiguration.TestConfigurationBeanFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
public class QuestionTextPossibilityTest {

  private final QuestionTextPossibilityRepository questionTextPossibilityRepository = Mockito.mock(QuestionTextPossibilityRepository.class);

  private QuestionTextPossibilityService questionTextPossibilityService;

  @Before
  public void setup(){
    questionTextPossibilityService = new QuestionTextPossibilityServiceImpl(questionTextPossibilityRepository);
  }

  @Test
  public void convertStringToQuestionTextPossibility_withValidString_returnListQuestionTextPossibility(){
    List<String> questionTextPossibilitiesStrings = new ArrayList<>();
    questionTextPossibilitiesStrings.add("QuestionText1");
    questionTextPossibilitiesStrings.add("QuestionText2");
    questionTextPossibilitiesStrings.add("QuestionText3");

    List<QuestionTextPossibility> questionTextPossibilities = questionTextPossibilityService.convertStringToQuestionTextPossibility(questionTextPossibilitiesStrings);

    Assert.assertEquals(3, questionTextPossibilities.size());
    Assert.assertEquals("QuestionText1", questionTextPossibilities.get(0).getAnswerText());
    Assert.assertEquals("QuestionText2", questionTextPossibilities.get(1).getAnswerText());
    Assert.assertEquals("QuestionText3", questionTextPossibilities.get(2).getAnswerText());
  }

  @Test(expected = NullPointerException.class)
  public void convertStringToQuestionTextPossibility_withNullStringValue_returnListQuestionTextPossibility(){
    List<String> questionTextPossibilitiesStrings = new ArrayList<>();
    questionTextPossibilitiesStrings.add("QuestionText1");
    questionTextPossibilitiesStrings.add("QuestionText2");
    questionTextPossibilitiesStrings.add(null);

    List<QuestionTextPossibility> questionTextPossibilities = questionTextPossibilityService.convertStringToQuestionTextPossibility(questionTextPossibilitiesStrings);
  }
}
