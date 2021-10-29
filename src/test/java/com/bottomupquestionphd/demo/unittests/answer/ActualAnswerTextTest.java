package com.bottomupquestionphd.demo.unittests.answer;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.repositories.ActualAnswerTextRepository;
import com.bottomupquestionphd.demo.services.answers.actualanswertexts.ActualAnswerTextService;
import com.bottomupquestionphd.demo.services.answers.actualanswertexts.ActualAnswerTextServiceImpl;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
public class ActualAnswerTextTest {

  private final ActualAnswerTextRepository actualAnswerTextRepository = Mockito.mock(ActualAnswerTextRepository.class);
  @Autowired
  private BeanFactory beanFactory;
  private ActualAnswerTextService actualAnswerTextService;

  @Before
  public void setup() {
    actualAnswerTextService = new ActualAnswerTextServiceImpl(actualAnswerTextRepository);
  }

  @Test
  public void setToBeDeleted_withValidInput() throws MissingParamsException {
    List<Long> answerIds = List.of(1l, 2l, 3l, 4l);

    actualAnswerTextService.setToDeleted(answerIds);
    Mockito.verify(actualAnswerTextRepository, times(answerIds.size())).setElementsToDeleted(any(Long.class));
  }

  @Test(expected = MissingParamsException.class)
  public void setToBeDeleted_withNullInput_throwsMissingParamsException() throws MissingParamsException {
    List<Long> answerIds = null;

    actualAnswerTextService.setToDeleted(answerIds);
  }

  @Test
  public void saveActualAnswer_withValidInput() throws MissingParamsException {
    ActualAnswerText actualAnswerText = (ActualAnswerText) beanFactory.getBean("actualAnswerText");

    actualAnswerTextService.saveActualAnswer(actualAnswerText);
    Mockito.verify(actualAnswerTextRepository, times(1)).saveAndFlush(actualAnswerText);
  }

  @Test(expected = MissingParamsException.class)
  public void saveActualAnswer_withNullInput_throwsMissingParamsException() throws MissingParamsException {
    ActualAnswerText actualAnswerText = null;

    actualAnswerTextService.saveActualAnswer(actualAnswerText);
  }

  @Test(expected = MissingParamsException.class)
  public void saveActualAnswer_withEmptyAnswerText_throwsMissingParamsException() throws MissingParamsException {
    ActualAnswerText actualAnswerText = new ActualAnswerText();
    actualAnswerText.setAnswerText("");

    actualAnswerTextService.saveActualAnswer(actualAnswerText);
  }

  @Test
  public void setActualAnswerTextsToAnswer_withValidInput_returnsListAnswer() throws MissingParamsException {
    List<Answer> answers = (List<Answer>) beanFactory.getBean("getAnswers");
    List<Answer> originalAnswers = (List<Answer>) beanFactory.getBean("getAnswers");

    List<Answer> returnedAnswers = actualAnswerTextService.setActualAnswerTextsToAnswer(answers, originalAnswers);

    Assert.assertEquals(returnedAnswers.size(), answers.size());
    Assert.assertEquals(returnedAnswers.get(0).getActualAnswerTexts().get(0).getAnswer(), answers.get(0));
    Assert.assertEquals(returnedAnswers.get(1).getActualAnswerTexts().get(0).getAnswer(), answers.get(1));
  }

  @Test(expected = MissingParamsException.class)
  public void setActualAnswerTextsToAnswer_withNullInputOnOriginalAnswers_throwsMissingParamsException() throws MissingParamsException {
    List<Answer> answers = null;
    List<Answer> originalAnswers = (List<Answer>) beanFactory.getBean("getAnswers");

    actualAnswerTextService.setActualAnswerTextsToAnswer(answers, originalAnswers);
  }

  @Test(expected = MissingParamsException.class)
  public void setActualAnswerTextsToAnswer_withNullInputOnAnswers_throwsMissingParamsException() throws MissingParamsException {
    List<Answer> originalAnswers = null;
    List<Answer> answers = (List<Answer>) beanFactory.getBean("getAnswers");

    actualAnswerTextService.setActualAnswerTextsToAnswer(answers, originalAnswers);
  }

  @Test
  public void setAnswerToBeDeleted_withValidInput() throws MissingParamsException {
    List<Long> answerIds = List.of(1l, 2l, 3l, 4l);

    actualAnswerTextService.setAnswerTextsToBeDeleted(answerIds);

    Mockito.verify(actualAnswerTextRepository, times(1)).setElementsToBeDeletedByMultipleAnswerIds(answerIds);
  }

  @Test(expected = MissingParamsException.class)
  public void setAnswerToBeDeleted_withNullInput_shouldThrowMissingParamsException() throws MissingParamsException {
    List<Long> answerIds = null;

    actualAnswerTextService.setAnswerTextsToBeDeleted(answerIds);
  }
}
