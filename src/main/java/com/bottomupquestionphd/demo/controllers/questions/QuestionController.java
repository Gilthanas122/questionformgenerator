package com.bottomupquestionphd.demo.controllers.questions;

import com.bottomupquestionphd.demo.domains.dtos.question.QuestionCreateDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidInputFormatException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionChangeException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionException;
import com.bottomupquestionphd.demo.exceptions.question.QuestionNotFoundByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormIsNullException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.services.questions.QuestionService;
import org.hibernate.TypeMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
@RequestMapping("question")
public class QuestionController {
  private static final Logger log = LoggerFactory.getLogger(QuestionController.class);
  private final QuestionService questionService;

  public QuestionController(QuestionService questionService) {
    this.questionService = questionService;
  }

  @GetMapping("/create/{questionFormId}")
  public String renderCreateQuestion(@PathVariable long questionFormId, Model model) {
    log.info("GET /create/" + questionFormId + " started");
    model.addAttribute("questionFormId", questionFormId);
    log.info("GET /create/" + questionFormId + " finished");
    return "question/create";
  }

  @PostMapping("/create/{type}/{questionFormId}")
  public String saveQuestion(Model model, @ModelAttribute QuestionCreateDTO questionDTO, @PathVariable String type, @PathVariable long questionFormId) {
    log.info("POST /create/" + type + "/" + questionFormId + " started");
    model.addAttribute("questionDTO", questionDTO);
    try {
      questionService.saveQuestion(type, questionDTO, questionFormId);
      log.info("POST /create/" + type + "/" + questionFormId + " finished");
      return "redirect:/question/create/" + questionFormId;
    } catch (MissingParamsException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (QuestionFormNotFoundException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (BelongToAnotherUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (MissingUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (InvalidInputFormatException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "question/create";
  }

  @GetMapping("update/{questionId}")
  public String renderQuestionUpdate(@PathVariable long questionId, Model model) {
    log.info("GET /update/" + questionId+ "/" + " started");
    try {
      QuestionWithDTypeDTO question = questionService.findByIdAndConvertToQuestionWithDTypeDTO(questionId);
      model.addAttribute("question", question);
      log.info("GET /update/" + questionId+ "/" + " finished");
      return "question/update";
    } catch (QuestionNotFoundByIdException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (BelongToAnotherUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/question-form/list";
  }

  @PostMapping("update/{questionId}")
  public String updateQuestionById(@ModelAttribute QuestionWithDTypeDTO question, Model model, @PathVariable long questionId) {
    log.info("POST /update/" + questionId+ "/" + " started");
    try {
      questionService.saveQuestionFromQuestionDType(question);
      log.info("POST /update/" + questionId+ "/" + " finished");
      return "redirect:/question-form/list-questions/" + questionService.findQuestionFormIdBelongingToQuestion(questionId);
    } catch (MissingParamsException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (QuestionNotFoundByIdException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (TypeMismatchException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/question/update/" + questionId;
  }

  @GetMapping("/update-position/{change}/{questionId}")
  public String updateListPosition(@PathVariable String change, @PathVariable long questionId, RedirectAttributes redirectAttributes) throws QuestionNotFoundByIdException {
    log.info("GET /update-position/" + change+ "/" + questionId + " started");
    try {
      long questionFormId = questionService.findQuestionFormIdBelongingToQuestion(questionId);
      questionService.changeOrderOfQuestion(change, questionId);
      log.info("GET /update-position/" + change+ "/" + questionId + " finished");
      return "redirect:/question-form/list-questions/" + questionFormId;
    } catch (InvalidQuestionPositionChangeException e) {
      log.error(e.getMessage());
      redirectAttributes.addAttribute("error", e.getMessage());
    } catch (InvalidQuestionPositionException e) {
      log.error(e.getMessage());
      redirectAttributes.addAttribute("error", e.getMessage());
    } catch (QuestionNotFoundByIdException e) {
      log.error(e.getMessage());
      redirectAttributes.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      redirectAttributes.addAttribute("error", e.getMessage());
    }
    return "app-user/landing-page";
  }

  @GetMapping("/delete/{questionId}")
  public String deleteQuestionById(@PathVariable long questionId, RedirectAttributes redirectAttributes) {
    log.info("GET /delete" + "/" + questionId + " started");
    try {
      long formId = questionService.deleteQuestion(questionId);
      log.info("GET /delete" + "/" + questionId + " finished");
      return "redirect:/question-form/list-questions/" + formId;
    } catch (QuestionNotFoundByIdException e) {
      log.error(e.getMessage());
      redirectAttributes.addAttribute("error", e.getMessage());
    } catch (BelongToAnotherUserException e) {
      log.error(e.getMessage());
      redirectAttributes.addAttribute("error", e.getMessage());
    } catch (QuestionFormIsNullException e) {
      log.error(e.getMessage());
      redirectAttributes.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      redirectAttributes.addAttribute("error", e.getMessage());
    }
    return "redirect:/question-form/list/";
  }

}
