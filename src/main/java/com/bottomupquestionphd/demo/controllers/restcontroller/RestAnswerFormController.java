package com.bottomupquestionphd.demo.controllers.restcontroller;

import com.bottomupquestionphd.demo.controllers.answers.AnswerFormController;
import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.dtos.answerform.CreateAnswerFormDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.answerform.*;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.services.answerforms.AnswerFormService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rest/answer-form")
@PreAuthorize("isAuthenticated()")
public class RestAnswerFormController {
  private static final Logger log = LoggerFactory.getLogger(AnswerFormController.class);
  private final AnswerFormService answerFormService;

  public RestAnswerFormController(AnswerFormService answerFormService) {
    this.answerFormService = answerFormService;
  }

  @GetMapping("create/{questionFormId}")
  public ResponseEntity<CreateAnswerFormDTO> renderCreateAnswerForm(@PathVariable long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, AnswerFormAlreadyFilledOutByCurrentUserException {
    log.info("RESTGET rest/answer-form/create/" + questionFormId + "started");
    CreateAnswerFormDTO createAnswerFormDTO = answerFormService.createAnswerFormDTO(questionFormId);
    log.info("REST GET rest/answer-form/create/" + questionFormId + "finished");

    return new ResponseEntity<>(createAnswerFormDTO, HttpStatus.OK);
  }

  @PostMapping("create/{questionFormId}/{appUserId}")
  public ResponseEntity<AnswerForm> submitAnswerForm(@RequestBody AnswerForm answerForm, @PathVariable long questionFormId, @PathVariable long appUserId) throws NoSuchUserByIdException, MissingUserException, QuestionFormNotFoundException, MissingParamsException, BelongToAnotherUserException, AnswerFormAlreadyFilledOutByCurrentUserException {
    log.info("REST POST rest/answer-form/create/" + "/" + questionFormId + "/" + appUserId + " started");
    AnswerForm answerFormReturned = answerFormService.saveAnswerForm(answerForm, questionFormId, appUserId);
    log.info("REST POST rest/answer-form/create/" + "/" + questionFormId + "/" + appUserId + " finished");

    return new ResponseEntity<>(answerFormReturned, HttpStatus.OK);
  }

  @GetMapping("/update/{questionFormId}/{answerFormId}/{appUserId}")
  public ResponseEntity<CreateAnswerFormDTO> updateAnswerFormCreatedByUser(@PathVariable long questionFormId, @PathVariable long answerFormId, @PathVariable long appUserId, Model model) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, AnswerFormAlreadyFilledOutByCurrentUserException, MissingParamsException, AnswerFormNotFilledOutException {
    log.info("REST GET rest/answer-form/update/" + questionFormId + "/" + appUserId + " started");
    CreateAnswerFormDTO createAnswerFormDTO = answerFormService.createAnswerFormToUpdate(questionFormId, appUserId);
    log.info("REST GET rest/answer-form/update/" + questionFormId + "/" + appUserId + " finished");

    return new ResponseEntity<>(createAnswerFormDTO, HttpStatus.OK);
  }

  @PutMapping("/update/{answerFormId}/{appUserId}")
  public ResponseEntity<AnswerForm> updateAnswerFormWithNewAnswers(@PathVariable long appUserId, @PathVariable long answerFormId,
                                               @RequestBody AnswerForm answerForm) throws NoSuchUserByIdException, NoSuchAnswerformById, MissingParamsException, BelongToAnotherUserException, NumberOfQuestionAndAnswersShouldMatchException, AnswerFormNumberOfAnswersShouldMatchException, AnswerFormNotFoundException {
    log.info("REST PUT rest/answer-form/update/" + answerFormId + "/" + appUserId + " started");
    AnswerForm answerFormReturned = answerFormService.saveUpdatedAnswerForm(answerFormId, appUserId, answerForm);
    log.info("REST PUT rest/answer-form/update/" + answerFormId + "/" + appUserId + " finished");

    return new ResponseEntity<>(answerFormReturned, HttpStatus.OK);
  }
}
