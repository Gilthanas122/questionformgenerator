package com.bottomupquestionphd.demo.controllers.restcontroller;

import com.bottomupquestionphd.demo.controllers.questions.QuestionController;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionCreateDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.question.*;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.services.questions.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
@RequestMapping("rest/question")
public class RestQuestionController {
  private static final Logger log = LoggerFactory.getLogger(QuestionController.class);
  private final QuestionService questionService;

  public RestQuestionController(QuestionService questionService) {
    this.questionService = questionService;
  }

  @GetMapping("/create/{questionFormId}")
  public ResponseEntity<Long> renderCreateQuestionRest(@PathVariable long questionFormId) {
    log.info("REST GET rest/question/create/" + questionFormId + " started");
    log.info("REST GET rest/question/create/" + questionFormId + " finished");

    return new ResponseEntity<>(questionFormId, HttpStatus.OK);
  }

  @PostMapping("/create/{type}/{questionFormId}")
  public ResponseEntity<?> saveQuestionRest(@RequestBody QuestionCreateDTO questionDTO, @PathVariable String type, @PathVariable long questionFormId) throws MissingUserException, InvalidInputFormatException, QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException {
    log.info("REST POST rest/question/create/" + type + "/" + questionFormId + " started");
    questionService.saveQuestion(type, questionDTO, questionFormId);
    log.info("REST POST rest/question/create/" + type + "/" + questionFormId + " finished");

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("update/{questionId}")
  public ResponseEntity<QuestionWithDTypeDTO> renderQuestionUpdateRest(@PathVariable long questionId) throws QuestionNotFoundByIdException, BelongToAnotherUserException, QuestionHasBeenAnsweredException {
    log.info("REST GET rest/question/update/" + questionId+ "/" + " started");
    QuestionWithDTypeDTO question = questionService.findByIdAndConvertToQuestionWithDTypeDTO(questionId);
    log.info("REST GET rest/question/update/" + questionId+ "/" + " finished");

    return new ResponseEntity<>(question, HttpStatus.OK);
  }

  @PutMapping("update")
  public ResponseEntity<Question> updateQuestionById(@RequestBody QuestionWithDTypeDTO question) throws QuestionNotFoundByIdException, MissingParamsException, BelongToAnotherUserException {
    log.info("REST POST rest/question/update/" + " started");
    Question questionReturned = questionService.saveQuestionFromQuestionDType(question);
    log.info("REST POST rest/question/update/" + " finished");

    return new ResponseEntity<>(questionReturned, HttpStatus.OK);
  }

  @PutMapping("/update-position/{change}/{questionId}")
  public ResponseEntity<Question> updateListPosition(@PathVariable String change, @PathVariable long questionId) throws QuestionNotFoundByIdException, InvalidQuestionPositionException, InvalidQuestionPositionChangeException, BelongToAnotherUserException {
    log.info("REST PUT rest/question/update-position/" + change+ "/" + questionId + " started");
    Question question = questionService.changeOrderOfQuestion(change, questionId);
    log.info("REST PUT rest/question/update-position/" + change+ "/" + questionId + " finished");

    return new ResponseEntity<>(question, HttpStatus.OK);
  }

  @DeleteMapping("/delete/{questionId}")
  public ResponseEntity<Long> deleteQuestionById(@PathVariable long questionId) throws QuestionNotFoundByIdException, BelongToAnotherUserException {
    log.info("REST DELETE rest/question/delete" + "/" + questionId + " started");
    long questionFormId = questionService.deleteQuestion(questionId);
    log.info("REST DELETE rest/question/delete" + "/" + questionId + " finished");

    return new ResponseEntity<>(questionFormId, HttpStatus.OK);
  }
}
