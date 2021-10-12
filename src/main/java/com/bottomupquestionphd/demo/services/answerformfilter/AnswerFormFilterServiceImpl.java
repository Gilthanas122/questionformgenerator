package com.bottomupquestionphd.demo.services.answerformfilter;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.bottomupquestionphd.demo.domains.daos.questions.QuestionType;
import com.bottomupquestionphd.demo.domains.dtos.answerformfilter.ActualAnswerTextSearchTermResultDTO;
import com.bottomupquestionphd.demo.domains.dtos.answerformfilter.AnswerSearchTermResultDTO;
import com.bottomupquestionphd.demo.domains.dtos.answerformfilter.QuestionFormAnswersExportExcelDTO;
import com.bottomupquestionphd.demo.domains.dtos.answerformfilter.SearchTermsForFilteringDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.ScaleQuestionFilterEnums;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionFormFilteringException;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionFormHasNotQuestionsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.services.answerforms.AnswerFormService;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import com.bottomupquestionphd.demo.services.namedparameterservice.QueryService;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerFormFilterServiceImpl implements AnswerFormFilterService {
  private final QuestionFormService questionFormService;
  private final AppUserService appUserService;
  private final QueryService queryService;
  private final AnswerFormService answerFormService;

  public AnswerFormFilterServiceImpl(QuestionFormService questionFormService, AppUserService appUserService, QueryService queryService, AnswerFormService answerFormService) {
    this.questionFormService = questionFormService;
    this.appUserService = appUserService;
    this.queryService = queryService;
    this.answerFormService = answerFormService;
  }

  @Override
  public QuestionForm generateSearchFields(long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, QuestionFormHasNotQuestionsException {
    QuestionForm questionForm = questionFormService.findById(questionFormId);
    if (questionForm.getQuestions() == null || questionForm.getQuestions().isEmpty()) {
      throw new QuestionFormHasNotQuestionsException("Can not search a question form without questions");
    }
    appUserService.checkIfCurrentUserMatchesUserIdInPath(questionForm.getAppUser().getId());
    return questionForm;
  }

  @Override
  public AnswerSearchTermResultDTO filterAnswers(long questionFormId, SearchTermsForFilteringDTO searchTermsForFilteringDTO) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, QuestionFormFilteringException {
    QuestionForm questionForm = questionFormService.findById(questionFormId);
    if (questionForm.getQuestions().size() != searchTermsForFilteringDTO.getSearchTerms().size()) {
      throw new QuestionFormFilteringException("Invalid amount of search terms submitted");
    }
    AnswerSearchTermResultDTO answerSearchTermResultDTO = new AnswerSearchTermResultDTO();
    return addActualAnswerTextToResult(questionForm, answerSearchTermResultDTO, searchTermsForFilteringDTO);
  }

  @Override
  public void returnAllAnswersBelongingToQuestionForm(long questionFormId, HttpServletResponse response) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, IOException {
    QuestionForm questionForm = questionFormService.findById(questionFormId);
    List<String> questionTexts = findQuestionTextsFromQuestionForm(questionForm.getQuestions());
    for (int i = 0; i <questionForm.getAnswerForms().size(); i++) {
      answerFormService.sortAnswersByQuestions(questionForm.getQuestions(), questionForm.getAnswerForms().get(i).getAnswers());
    }
    List<List<String>> answerTexts = createListStringFromAnswerFormsAnswers(questionForm.getAnswerForms());
    response.setContentType("application/octet-stream");
    String headerKey = "Content-Disposition";
    String headerValue = "attachement; filename=answers.xlsx";
    response.setHeader(headerKey, headerValue);
    QuestionFormAnswersExportExcelDTO questionFormAnswersExportExcelDTO = new QuestionFormAnswersExportExcelDTO(questionTexts, answerTexts);
    questionFormAnswersExportExcelDTO.export(response);
  }

  private List<List<String>> createListStringFromAnswerFormsAnswers(List<AnswerForm> answerForms) {
    List<List<String>> results = new ArrayList<>();
    List<String> actualAnswerTexts = new ArrayList<>();
    for (AnswerForm answerForm : answerForms) {
      for (int i = 0; i < answerForm.getAnswers().size(); i++) {
        Answer answer = answerForm.getAnswers().get(i);
        StringBuilder result = new StringBuilder();
        for (int j = 0; j < answer.getActualAnswerTexts().size(); j++) {
          StringBuilder builder = new StringBuilder(answer.getActualAnswerTexts().get(j).getAnswerText());
          builder.append(";;");
          result.append(builder);
        }
        if (result.length() > 3){
          actualAnswerTexts.add(result.substring(0, result.length() - 2));
        }else{
          actualAnswerTexts.add("no answer provided");
        }
      }
      results.add(actualAnswerTexts);
    }
    return results;
  }

  private List<String> findQuestionTextsFromQuestionForm(List<Question> questions) {
    return questions
            .stream()
            .map(Question::getQuestionText)
            .collect(Collectors.toList());
  }

  private AnswerSearchTermResultDTO addActualAnswerTextToResult(QuestionForm questionForm, AnswerSearchTermResultDTO answerSearchTermResultDTO, SearchTermsForFilteringDTO searchTermsForFilteringDTO) throws BelongToAnotherUserException {
    appUserService.checkIfCurrentUserMatchesUserIdInPath(questionForm.getAppUser().getId());
    for (int i = 0; i < searchTermsForFilteringDTO.getSearchTerms().size(); i++) {
      for (Question actualQuestion : questionForm.getQuestions()){
        String actualSearchTerm = searchTermsForFilteringDTO.getSearchTerms().get(i);
        ActualAnswerTextSearchTermResultDTO actualAnswerTextSearchTermResultDTO = new ActualAnswerTextSearchTermResultDTO();
        String questionType = actualQuestion.getDiscriminatorValue();
        for (Answer answer : actualQuestion.getAnswers()) {
          actualAnswerTextSearchTermResultDTO.setAnswerId(answer.getId());
          actualAnswerTextSearchTermResultDTO.setQuestionType(actualQuestion.getDiscriminatorValue());
          answerSearchTermResultDTO.setAppUserId(answer.getAnswerForm().getAppUser().getId());
          if (questionType.matches("CheckBoxQuestion|RadioButtonQuestion|TextQuestion")) {
            actualAnswerTextSearchTermResultDTO = getAnswersForCheckBoxOrRadiobuttonOrTextQuestion(actualSearchTerm, answer, questionType, answerSearchTermResultDTO);
          } else if (questionType.equals(QuestionType.SCALEQUESTION.toString())) {
            actualAnswerTextSearchTermResultDTO = getAnswersForScaleQuestion(actualSearchTerm, answer, actualAnswerTextSearchTermResultDTO);
          }
          if (!actualAnswerTextSearchTermResultDTO.getActualAnswers().isEmpty()){
            answerSearchTermResultDTO.getActualAnswerTextSearchTermResultDTOS().add(actualAnswerTextSearchTermResultDTO);
          }
        }
      }
    }
    return answerSearchTermResultDTO;
  }

  private ActualAnswerTextSearchTermResultDTO getAnswersForScaleQuestion(String actualSearchTerm, Answer answer, ActualAnswerTextSearchTermResultDTO actualAnswerTextSearchTermResultDTO) {
    String operator = ScaleQuestionFilterEnums.checkIfSearchTermContainsOperator(actualSearchTerm);
    List<String> filteredActualAnswerTexts = queryService.filterActualAnswerTextsForScaleQuestion(answer.getId(), operator, removeOperatorsFromActualSearchTerm(actualSearchTerm, operator));
    actualAnswerTextSearchTermResultDTO.setActualAnswers(filteredActualAnswerTexts);
    return actualAnswerTextSearchTermResultDTO;
  }

  private ActualAnswerTextSearchTermResultDTO getAnswersForCheckBoxOrRadiobuttonOrTextQuestion(String actualSearchTerm, Answer answer, String questionType, AnswerSearchTermResultDTO answerSearchTermResultDTO) {
    List<String> filteredActualAnswerTexts = filterActualAnswerTextsBelongingToAnswer(answer.getActualAnswerTexts(), actualSearchTerm);
    ActualAnswerTextSearchTermResultDTO actualAnswerTextSearchTermResultDTO = new ActualAnswerTextSearchTermResultDTO();
    if (filteredActualAnswerTexts.size() > 0) {
      actualAnswerTextSearchTermResultDTO.setActualAnswers(filteredActualAnswerTexts);
    }
    if (questionType.equals("TextQuestion")) {
      setAverageForActualAnswerTextsTextAnswerVotes(answerSearchTermResultDTO, answer);
    }
    return actualAnswerTextSearchTermResultDTO;
  }

  private String removeOperatorsFromActualSearchTerm(String actualSearchTerm, String operator) {
    if (actualSearchTerm.equals("=")) {
      return actualSearchTerm;
    }
    return actualSearchTerm.replace(operator, "");
  }

  private List<String> filterActualAnswerTextsBelongingToAnswer(List<ActualAnswerText> filtereActualAnswerTexts, String actualSearchTerm) {
    List<String> actualAnswerTexts = new ArrayList<>();
    String[] actualSearchTerms = new String[1];
    if (actualSearchTerm.contains("AND")) {
      actualSearchTerms[0] = convertActualSearchTermWithAndToSearchTerm(actualSearchTerm);
    } else if (actualSearchTerm.contains("OR")) {
      actualSearchTerms = actualSearchTerm.split("OR");
    } else {
      actualSearchTerms[0] = actualSearchTerm;
    }
    for (String searchTerm : actualSearchTerms) {
      actualAnswerTexts.addAll(filtereActualAnswerTexts
              .stream()
              .filter(answerText -> !searchTerm.isBlank() && answerText.getAnswerText().contains(searchTerm))
              .map(ActualAnswerText::getAnswerText)
              .collect(Collectors.toList()));
    }
    return actualAnswerTexts;
  }

  private String convertActualSearchTermWithAndToSearchTerm(String actualSearchTerm) {
    StringBuilder result = new StringBuilder();
    String[] searchTerms = actualSearchTerm.split("AND");
    for (String searchTerm : searchTerms) {
      result.append(searchTerm).append(" ");
    }
    return result.toString().trim();
  }

  private AnswerSearchTermResultDTO setAverageForActualAnswerTextsTextAnswerVotes(AnswerSearchTermResultDTO answerSearchTermResultDTO, Answer answer) {
    List<Double> averages = answer.getActualAnswerTexts().stream().mapToDouble(ActualAnswerText::getAverageOfTextAnswerVotes).boxed().collect(Collectors.toList());
    for (int i = 0; i < answerSearchTermResultDTO.getActualAnswerTextSearchTermResultDTOS().size(); i++) {
      ActualAnswerTextSearchTermResultDTO actualAnswerTextSearchTermResultDTO = answerSearchTermResultDTO.getActualAnswerTextSearchTermResultDTOS().get(i);
      actualAnswerTextSearchTermResultDTO.setTextAnswerVotesAverage(averages.get(0));
    }
    return answerSearchTermResultDTO;
  }
}
