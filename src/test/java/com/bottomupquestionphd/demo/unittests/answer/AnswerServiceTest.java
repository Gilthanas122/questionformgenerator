package com.bottomupquestionphd.demo.unittests.answer;

import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.CheckBoxQuestion;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.AnswerRepository;
import com.bottomupquestionphd.demo.services.actualanswertexts.ActualAnswerTextService;
import com.bottomupquestionphd.demo.services.answers.AnswerService;
import com.bottomupquestionphd.demo.services.answers.AnswerServiceImpl;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
public class AnswerServiceTest {

  private final AnswerRepository answerRepository = Mockito.mock(AnswerRepository.class);
  private final QuestionFormService questionFormService = Mockito.mock(QuestionFormService.class);
  private final ActualAnswerTextService actualAnswerTextService = Mockito.mock(ActualAnswerTextService.class);

  @Autowired
  private BeanFactory beanFactory;

  private AnswerService answerService;

  @Before
  public void setup() {
    answerService = new AnswerServiceImpl(answerRepository, questionFormService, actualAnswerTextService);
  }

  @Test
  public void connectQuestionsToAnswers_withValidInputData() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException {
    List<Answer> answers = (List<Answer>) beanFactory.getBean("getAnswers");
    long questionFormId = 1;
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");

    Mockito.when(questionFormService.findByIdForAnswerForm(questionFormId)).thenReturn(questionForm);

    answerService.connectQuestionsToAnswers(answers, questionFormId);

    Mockito.verify(answerRepository, times(answers.size())).saveAndFlush(any(Answer.class));
  }

  @Test(expected = MissingParamsException.class)
  public void connectQuestionsToAnswers_withNullListAnswers_throwsMissingParamsException() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException {
    List<Answer> answers = null;
    long questionFormId = 1;
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionForm");

    Mockito.when(questionFormService.findByIdForAnswerForm(questionFormId)).thenReturn(questionForm);

    answerService.connectQuestionsToAnswers(answers, questionFormId);

    Mockito.verify(answerRepository, times(answers.size())).saveAndFlush(any(Answer.class));
  }

  @Test
  public void connectAnswersToActualAnswers_withValidInputData() throws MissingParamsException {
    List<Answer> answers = (List<Answer>) beanFactory.getBean("getAnswers");

    answerService.connectAnswersToActualAnswers(answers);

    Assert.assertEquals(answers.get(0).getActualAnswerTexts().get(0).getAnswer(), answers.get(0));
    Assert.assertEquals(answers.get(1).getActualAnswerTexts().get(0).getAnswer(), answers.get(1));
  }

  @Test(expected = MissingParamsException.class)
  public void connectAnswersToActualAnswers_withNullInput_throwsMissingParamsException() throws MissingParamsException {
    List<Answer> answers = null;

    answerService.connectAnswersToActualAnswers(answers);
  }

  @Test
  public void setActualAnswersToDeleted_withValidInputData() throws MissingParamsException {
    long appUserId = 1;
    long questionFormId = 1;
    List<Long> answersIdsToBeDeleted = List.of(1l, 2l, 3l, 4l);

    Mockito.when(answerRepository.findAllAnswerToBeDeleted(appUserId)).thenReturn(answersIdsToBeDeleted);

    answerService.setActualAnswersToDeleted(appUserId, questionFormId);

    Mockito.verify(answerRepository, times(1)).setAnswerToDeletedByQuestionFormId(questionFormId);
    Mockito.verify(actualAnswerTextService, times(1)).setToDeleted(answersIdsToBeDeleted);

  }

  @Test
  public void setAnswersToAnswerForm_withValidInputData() throws MissingParamsException {
    AnswerForm answerForm = (AnswerForm) beanFactory.getBean("answerForm");
    CheckBoxQuestion question = (CheckBoxQuestion) beanFactory.getBean("checkboxQuestion");
    List<Answer> answers = (List<Answer>) beanFactory.getBean("getAnswers");
    List<Answer> originalAnswers = (List<Answer>) beanFactory.getBean("getAnswers");
    for (int i = 0; i < originalAnswers.size(); i++) {
      originalAnswers.get(i).setQuestion(question);
    }

    Mockito.when(actualAnswerTextService.setActualAnswerTextsToAnswer(answers, originalAnswers)).thenReturn(originalAnswers);

    answerService.setAnswersToAnswerForm(answerForm, answers, originalAnswers);

    Mockito.verify(actualAnswerTextService, times(1)).setActualAnswerTextsToAnswer(answers, originalAnswers);
    Assert.assertEquals(answers.get(0).getQuestion().getQuestionText(), question.getQuestionText());
  }

  @Test(expected = MissingParamsException.class)
  public void setAnswersToAnswerForm_withNullAsInput_throwsMissingParamsException() throws MissingParamsException {
    AnswerForm answerForm = null;
    List<Answer> answers = (List<Answer>) beanFactory.getBean("getAnswers");
    List<Answer> originalAnswers = (List<Answer>) beanFactory.getBean("getAnswers");

    answerService.setAnswersToAnswerForm(answerForm, answers, originalAnswers);
  }

  @Test
  public void findAllAnswerTextsBelongingToAQuestion_withValidInputData() throws MissingParamsException {
    List<Long> questionIds = List.of(1l, 2l, 3l, 4l);
    List<Answer> answers = (List<Answer>) beanFactory.getBean("getAnswers");


    Mockito.when(answerRepository.findAllByQuestionIds(questionIds)).thenReturn(answers);

    List<Answer> returnedAnswers = answerService.findAllAnswerTextsBelongingToAQuestion(questionIds);

    Assert.assertEquals(answers.size(), returnedAnswers.size());
    Assert.assertEquals(answers.get(0).getQuestion().getQuestionText(), returnedAnswers.get(0).getQuestion().getQuestionText());
  }

  @Test(expected = MissingParamsException.class)
  public void findAllAnswerTextsBelongingToAQuestion_withNullInput_throwsMissingParamsException() throws MissingParamsException {
    List<Long> questionIds = null;

    answerService.findAllAnswerTextsBelongingToAQuestion(questionIds);
  }

  @Test
  public void removeOwnAATextsFromAATToBeVoted_withValidInput_returnsListAnswers() throws MissingParamsException {
    List<Answer> answers = (List<Answer>) beanFactory.getBean("getAnswers");
    long appUserId = 11;
    for (int i = 1; i < answers.size(); i++) {
      answers.get(i).getAnswerForm().getAppUser().setId(appUserId);
    }

    List<Answer> returnedAnswers = answerService.removeOwnAATextsFromAATToBeVoted(appUserId, answers);

    Assert.assertEquals(1, returnedAnswers.size());
  }

  @Test(expected = MissingParamsException.class)
  public void removeOwnAATextsFromAATToBeVoted_nullInputAnswerList_throwsMissingParamsException() throws MissingParamsException {
    List<Answer> answers = null;
    long appUserId = 11;

    answerService.removeOwnAATextsFromAATToBeVoted(appUserId, answers);
  }
}
