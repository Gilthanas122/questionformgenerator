package com.bottomupquestionphd.demo.controllers.restcontroller;

import com.bottomupquestionphd.demo.controllers.questions.QuestionFormController;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.SuccessMessageDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormCreateDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserNameException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.NotEnoughQuestionsToCreateFormException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNameAlreadyExistsException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest/question-form")
@PreAuthorize("hasRole('ROLE_TEACHER')")
public class RESTQuestionFormController {
  private static final Logger log = LoggerFactory.getLogger(QuestionFormController.class);
  private final QuestionFormService questionFormService;

  public RESTQuestionFormController(QuestionFormService questionFormService) {
    this.questionFormService = questionFormService;
  }

  @GetMapping("create")
  public ResponseEntity<?> renderCreateQuestionForm() {
    log.info("REST GET question-form/create started");
    log.info("REST GET question-form/create finished");

    return new ResponseEntity<>(new QuestionForm(), HttpStatus.OK);
  }

  @PostMapping( "create")
  public ResponseEntity<?> saveQuestionForm(@RequestBody QuestionFormCreateDTO questionFormCreateDTO) throws NoSuchUserNameException, MissingParamsException, QuestionFormNameAlreadyExistsException {
    log.info("REST POST question-form/create started");
    long questionFormId = questionFormService.save(questionFormCreateDTO);
    log.info("REST POST question-form/create finished");

    return new ResponseEntity<>(questionFormId, HttpStatus.OK);
  }

  @SneakyThrows
  @GetMapping("list")
  public ResponseEntity<?> listTeachersQuestionForms() {
    log.info("REST GET question-form/list started");
    List<QuestionForm> questionForms = questionFormService.findAll();
    log.info("REST GET question-form/list finished");
    return new ResponseEntity<>(questionForms, HttpStatus.OK);
  }

  @GetMapping("update/{id}")
  public ResponseEntity<?> modifyQuestionForm(@PathVariable long id) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
    log.info("REST GET question-form/update" + id + " started");
    QuestionForm questionForm = questionFormService.findById(id);
    log.info("REST GET question-form/update" + id + " finished");

    return new ResponseEntity<>(questionForm, HttpStatus.OK);
  }

  @PutMapping("/finish/{questionFormId}")
  public ResponseEntity<?> finishQuestionForm(@PathVariable long questionFormId) throws NotEnoughQuestionsToCreateFormException, MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
    log.info("REST PUT question-form/finished" + questionFormId + " started");
    questionFormService.finishQuestionForm(questionFormId);
    log.info("REST PUT question-form/finished" + questionFormId + " finished");

    return new ResponseEntity<>(questionFormId, HttpStatus.OK);
  }

  @GetMapping("/list-questions/{questionFormId}")
  public ResponseEntity<?> listAllQuestionsBelongingToQuestionFormById(@PathVariable long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
    log.info("REST GET question-form/list-questions/" + questionFormId + " started");
    List<QuestionWithDTypeDTO> questionWithDTypeDTOS = questionFormService.findByIdAndAddQuestionType(questionFormId);
    log.info("REST GET question-form/list-questions/" + questionFormId + " finished");

    return new ResponseEntity<>(questionWithDTypeDTOS, HttpStatus.OK);
  }

  @DeleteMapping("delete/{questionFormId}")
  public ResponseEntity<?> deleteQuestionForm(@PathVariable long questionFormId) throws QuestionFormNotFoundException {
    log.info("REST DELETE question-form/delete/" + questionFormId + " started");
    questionFormService.deleteQuestionForm(questionFormId);
    log.info("REST DELETE question-form/delete/" + questionFormId + " finished");

    return new ResponseEntity<>(new SuccessMessageDTO("deleted", "Question Form with " + questionFormId + "successfully deleted"), HttpStatus.OK);
  }
}
