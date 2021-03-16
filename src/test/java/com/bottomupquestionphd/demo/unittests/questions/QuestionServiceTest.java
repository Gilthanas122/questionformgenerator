package com.bottomupquestionphd.demo.unittests.questions;

import com.bottomupquestionphd.demo.repositories.QuestionRepository;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import com.bottomupquestionphd.demo.services.questions.QuestionConversionService;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import com.bottomupquestionphd.demo.services.questions.QuestionService;
import com.bottomupquestionphd.demo.services.questions.QuestionServiceImpl;
import com.bottomupquestionphd.demo.services.questiontextpossibilities.QuestionTextPossibilityService;
import com.bottomupquestionphd.demo.testconfiguration.TestConfigurationBeanFactory;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
public class QuestionServiceTest {

  private final QuestionRepository questionRepository = Mockito.mock(QuestionRepository.class);
  private final QuestionFormService questionFormService = Mockito.mock(QuestionFormService.class);
  private final QuestionTextPossibilityService questionTextPossibilityService = Mockito.mock(QuestionTextPossibilityService.class);
  private final AppUserService appUserService = Mockito.mock(AppUserService.class);
  private final QuestionConversionService questionConversionService = Mockito.mock(QuestionConversionService.class);
  private QuestionService questionService;
  @Autowired
  private BeanFactory beanFactory;

  @Before
  void setup(){
    questionService = new QuestionServiceImpl(questionRepository, questionFormService, questionTextPossibilityService, appUserService, questionConversionService);
  }


}
