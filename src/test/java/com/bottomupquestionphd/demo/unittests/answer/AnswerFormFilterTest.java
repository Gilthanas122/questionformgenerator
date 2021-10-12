package com.bottomupquestionphd.demo.unittests.answer;

import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.answerformfilter.AnswerSearchTermResultDTO;
import com.bottomupquestionphd.demo.domains.dtos.answerformfilter.SearchTermsForFilteringDTO;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionFormFilteringException;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionFormHasNotQuestionsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.services.answerformfilter.AnswerFormFilterService;
import com.bottomupquestionphd.demo.services.answerformfilter.AnswerFormFilterServiceImpl;
import com.bottomupquestionphd.demo.services.answerforms.AnswerFormService;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import com.bottomupquestionphd.demo.services.namedparameterservice.QueryService;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import com.bottomupquestionphd.demo.testconfiguration.TestConfigurationBeanFactory;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
public class AnswerFormFilterTest {
  private final QuestionFormService questionFormService = Mockito.mock(QuestionFormService.class);
  private final AppUserService appUserService = Mockito.mock(AppUserService.class);
  private final QueryService queryService = Mockito.mock(QueryService.class);
  private final AnswerFormService answerFormService = Mockito.mock(AnswerFormService.class);

  private AnswerFormFilterService answerFormFilterService;

  @Autowired
  private BeanFactory beanFactory;

  @Before
  public void setup() {
    this.answerFormFilterService = new AnswerFormFilterServiceImpl(questionFormService, appUserService, queryService, answerFormService);
  }

  @Test
  public void generateSearchFields_withValidDate_shouldReturnQuestionForm() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, QuestionFormHasNotQuestionsException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionFormWithAnswerForm");
    long questionFormId = questionForm.getId();

    Mockito.when(questionFormService.findById(questionFormId)).thenReturn(questionForm);

    QuestionForm questionFormReturned = answerFormFilterService.generateSearchFields(questionFormId);

    assertEquals(questionForm.getQuestions().size(), questionFormReturned.getQuestions().size());
    assertEquals(questionForm.getQuestions().get(0).getQuestionText(), questionFormReturned.getQuestions().get(0).getQuestionText());
  }

  @Test(expected = QuestionFormHasNotQuestionsException.class)
  public void generateSearchFields_withNotQuestionInQuestionForm_shouldThrowQuestionFormHasNotQuestionsException() throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, QuestionFormHasNotQuestionsException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionFormWithAnswerForm");
    questionForm.setQuestions(new ArrayList<>());
    long questionFormId = questionForm.getId();

    Mockito.when(questionFormService.findById(questionFormId)).thenReturn(questionForm);

    answerFormFilterService.generateSearchFields(questionFormId);
  }

  @Test
  public void filterAnswers_withValidDataTextQuestion_shouldReturnAnswerSearchTermResultDTO() throws QuestionFormFilteringException, MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionFormWithAnswerFormWitnTextQuestions");
    long questionFormId = questionForm.getId();
    List<String> searchTerms = List.of("actualAnswerText0", "actualAnswerText1", "kaka", "kaki1");
    SearchTermsForFilteringDTO searchTermsForFilteringDTO = new SearchTermsForFilteringDTO(searchTerms);

    Mockito.when(questionFormService.findById(questionFormId)).thenReturn(questionForm);
    AnswerSearchTermResultDTO answerSearchTermResultDTO = answerFormFilterService.filterAnswers(questionFormId, searchTermsForFilteringDTO);
    assertEquals(8, answerSearchTermResultDTO.getActualAnswerTextSearchTermResultDTOS().size());
    assertEquals("actualAnswerText0", answerSearchTermResultDTO.getActualAnswerTextSearchTermResultDTOS().get(0).getActualAnswers().get(0));
    assertEquals("actualAnswerText1", answerSearchTermResultDTO.getActualAnswerTextSearchTermResultDTOS().get(4).getActualAnswers().get(0));
  }

  @Test
  public void filterAnswers_withValidDataCheckBoxQuestion_shouldReturnAnswerSearchTermResultDTO() throws QuestionFormFilteringException, MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionFormWithAnswerFormCheckBoxQuestions");
    long questionFormId = questionForm.getId();
    List<String> searchTerms = List.of("checkbox1", "checkbox2", "kaka", "kaki1");
    SearchTermsForFilteringDTO searchTermsForFilteringDTO = new SearchTermsForFilteringDTO(searchTerms);

    Mockito.when(questionFormService.findById(questionFormId)).thenReturn(questionForm);
    AnswerSearchTermResultDTO answerSearchTermResultDTO = answerFormFilterService.filterAnswers(questionFormId, searchTermsForFilteringDTO);
    assertEquals(32, answerSearchTermResultDTO.getActualAnswerTextSearchTermResultDTOS().size());
    assertEquals("checkbox1", answerSearchTermResultDTO.getActualAnswerTextSearchTermResultDTOS().get(0).getActualAnswers().get(0));
    assertEquals("checkbox2", answerSearchTermResultDTO.getActualAnswerTextSearchTermResultDTOS().get(16).getActualAnswers().get(0));
  }

  @Test
  public void filterAnswers_withValidDataScaleQuestion_shouldReturnAnswerSearchTermResultDTO() throws QuestionFormFilteringException, MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionFormWithAnswerFormScaleQuestion");
    long questionFormId = questionForm.getId();
    List<String> searchTerms = List.of("1", "5", "7", "8");
    SearchTermsForFilteringDTO searchTermsForFilteringDTO = new SearchTermsForFilteringDTO(searchTerms);

    Mockito.when(questionFormService.findById(questionFormId)).thenReturn(questionForm);
    Mockito.when(queryService.filterActualAnswerTextsForScaleQuestion(anyLong(), anyString(), anyString())).thenReturn(List.of("1"));
    AnswerSearchTermResultDTO answerSearchTermResultDTO = answerFormFilterService.filterAnswers(questionFormId, searchTermsForFilteringDTO);
    assertEquals(64, answerSearchTermResultDTO.getActualAnswerTextSearchTermResultDTOS().size());
    assertEquals("1", answerSearchTermResultDTO.getActualAnswerTextSearchTermResultDTOS().get(0).getActualAnswers().get(0));
    assertEquals("1", answerSearchTermResultDTO.getActualAnswerTextSearchTermResultDTOS().get(16).getActualAnswers().get(0));
  }

  @Test(expected = QuestionFormFilteringException.class)
  public void filterAnswers_withInvalidAmountOfSearchTerm_shouldThrowQuestionFormFilteringException() throws QuestionFormFilteringException, MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
    QuestionForm questionForm = (QuestionForm) beanFactory.getBean("questionFormWithAnswerForm");
    long questionFormId = questionForm.getId();
    List<String> searchTerms = List.of("actualAnswerText0", "actualAnswerText1");
    SearchTermsForFilteringDTO searchTermsForFilteringDTO = new SearchTermsForFilteringDTO(searchTerms);

    Mockito.when(questionFormService.findById(questionFormId)).thenReturn(questionForm);
    answerFormFilterService.filterAnswers(questionFormId, searchTermsForFilteringDTO);
  }
}
