package com.bottomupquestionphd.demo.controllers.questions;

import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormCreateDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserNameException;
import com.bottomupquestionphd.demo.exceptions.questionform.*;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
@RequestMapping("question-form")
public class QuestionFormController {
  private static final Logger log = LoggerFactory.getLogger(QuestionFormController.class);
  private final QuestionFormService questionFormService;

  public QuestionFormController(QuestionFormService questionFormService) {
    this.questionFormService = questionFormService;
  }

  @GetMapping("create")
  public String renderCreateQuestionForm(Model model) {
    log.info("GET question-form/create started");
    model.addAttribute("questionForm", new QuestionForm());
    log.info("GET question-form/create finished");
    return "questionform/create";
  }

  @PostMapping("create")
  public String saveQuestionForm(@ModelAttribute QuestionFormCreateDTO questionForm, Model model) {
    log.info("POST question-form/create started");
    model.addAttribute("questionFormDTO", questionForm);
    try {
      long questionFormId = questionFormService.save(questionForm);
      log.info("POST question-form/create finished");
      return "redirect:/question/create/" + questionFormId;
    } catch (MissingParamsException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (QuestionFormNameAlreadyExistsException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (NoSuchUserNameException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "questionform/create";
  }

  @GetMapping("list")
  public String listTeachersQuestionForms(Model model) {
    log.info("GET question-form/list started");
    try {
      model.addAttribute("questionForms", questionFormService.findAll());
    } catch (NoQuestionFormsInDatabaseException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    log.info("GET question-form/list finished");
    return "questionform/list";
  }

  @GetMapping("update/{id}")
  public String modifyQuestionForm(@PathVariable long id, Model model) {
    log.info("GET question-form/update" + id + " started");
    try {
      model.addAttribute("questionForm", questionFormService.findById(id));
      log.info("GET question-form/update" + id + " finished");
      return "questionform/create";
    } catch (BelongToAnotherUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (QuestionFormNotFoundException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (MissingUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "questionform/list";
  }

  @GetMapping("/finish/{questionFormId}")
  public String finishQuestionForm(@PathVariable long questionFormId, Model model) {
    log.info("GET question-form/finished" + questionFormId + " started");
    try {
      model.addAttribute("questionFormId", questionFormId);
      questionFormService.finishQuestionForm(questionFormId);
      model.addAttribute("successMessage", "Question Form successfully created");
      log.info("GET question-form/finished" + questionFormId + " finished");
      return "app-user/landing-page";
    } catch (MissingUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (QuestionFormNotFoundException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (BelongToAnotherUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (NotEnoughQuestionsToCreateFormException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "question/create";
  }

  @GetMapping("/list-questions/{questionFormId}")
  public String listAllQuestionsBelongingToQuestionFormById(@RequestParam(required = false) String error, @PathVariable long questionFormId, Model model) {
    log.info("GET question-form/list-questions/" + questionFormId + " started");
    try {
      model.addAttribute("questions", questionFormService.findByIdAndAddQuestionType(questionFormId) );
      model.addAttribute("error", error);
      log.info("GET question-form/list-questions/" + questionFormId + " finished");
      return "questionform/list-questions";
    } catch (MissingUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (BelongToAnotherUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (QuestionFormNotFoundException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "questionform/list";
  }

  @GetMapping("delete/{questionFormId}")
  public String deleteQuestionForm(@PathVariable long questionFormId, Model model){
    log.info("GET question-form/delete/" + questionFormId + " started");
    try {
      questionFormService.deleteQuestionForm(questionFormId);
      model.addAttribute("success", "Question Form with " + questionFormId + "successfully deleted");
    }catch (QuestionFormNotFoundException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (Exception e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    log.info("GET question-form/delete/" + questionFormId + " finished");
    return "redirect:/question-form/list";
  }
}
