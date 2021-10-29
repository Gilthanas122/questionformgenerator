package com.bottomupquestionphd.demo.controllers.restcontroller;

import com.bottomupquestionphd.demo.controllers.answers.AnswerFormController;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.answerformfilter.AnswerSearchTermResultDTO;
import com.bottomupquestionphd.demo.domains.dtos.answerformfilter.SearchTermsForFilteringDTO;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionFormFilteringException;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionFormHasNotQuestionsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.services.answers.answerformfilter.AnswerFormFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("rest/answer-form-filter")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
public class RESTAnswerFormFilterController {
  private static final Logger log = LoggerFactory.getLogger(AnswerFormController.class);
  private final AnswerFormFilterService answerFormFilterService;

  public RESTAnswerFormFilterController(AnswerFormFilterService answerFormFilterService) {
    this.answerFormFilterService = answerFormFilterService;
  }

  @GetMapping("search/{questionFormId}")
  public ResponseEntity<QuestionForm> renderSearchFormForQuestions(@PathVariable long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, QuestionFormHasNotQuestionsException {
    log.info("REST GET rest/answer-form-filter/search started");
    QuestionForm questionForm = answerFormFilterService.generateSearchFields(questionFormId);
    log.info("REST GET rest/answer-form-filter/search finished");
    return new ResponseEntity<>(questionForm, HttpStatus.OK);

  }

  @PostMapping("search/{questionFormId}")
  public ResponseEntity<AnswerSearchTermResultDTO> postFilterAnswers(@PathVariable long questionFormId, @RequestBody SearchTermsForFilteringDTO searchTermsForFilteringDTO) throws QuestionFormFilteringException, MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
    log.info("REST POST rest/answer-form/filter/search started");
    AnswerSearchTermResultDTO answerSearchTermResultDTO = answerFormFilterService.filterAnswers(questionFormId, searchTermsForFilteringDTO);
    log.info("REST POST rest/answer-form/filter/search finished");
    return new ResponseEntity<>(answerSearchTermResultDTO, HttpStatus.OK);
  }

  @GetMapping("/download-all-answers/{questionFormId}")
  public ResponseEntity<?> returnAllAnswers(@PathVariable long questionFormId, HttpServletResponse response) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, IOException {
    log.info("REST GET rest/answer-form/filter/return-all-answers/" + questionFormId + " started");
    answerFormFilterService.returnAllAnswersBelongingToQuestionForm(questionFormId, response);
    log.info("REST GET rest/answer-form/filter/return-all-answers/" + questionFormId + " finished");
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
