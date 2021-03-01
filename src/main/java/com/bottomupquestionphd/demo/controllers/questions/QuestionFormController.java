package com.bottomupquestionphd.demo.controllers.questions;

import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserNameException;
import com.bottomupquestionphd.demo.exceptions.questionform.*;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
@RequestMapping("question-form")
public class QuestionFormController {
  private final QuestionFormService questionFormService;

  public QuestionFormController(QuestionFormService questionFormService) {
    this.questionFormService = questionFormService;
  }

  @GetMapping("create")
  public String renderCreateQuestionForm(Model model) {
    model.addAttribute("questionForm", new QuestionForm());
    return "questionform/create";
  }

  @PostMapping("create")
  public String saveQuestionForm(@ModelAttribute QuestionForm questionForm, Model model) {
    model.addAttribute("questionFormDTO", questionForm);
    try {
      long questionFormId = questionFormService.save(questionForm);
      return "redirect:/question/create/" + questionFormId;
    } catch (MissingParamsException e) {
      model.addAttribute("error", e.getMessage());
    } catch (QuestionFormNameAlreadyExistsException e) {
      model.addAttribute("error", e.getMessage());
    } catch (NoSuchUserNameException e) {
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "questionform/create";
  }

  @GetMapping("list")
  public String listTeachersQuestionForms(Model model) {
    try {
      model.addAttribute("questionForms", questionFormService.findAll());
    } catch (NoQuestionFormsInDatabaseException e) {
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "questionform/list";
  }

  @GetMapping("update/{id}")
  public String modifyQuestionForm(@PathVariable long id, Model model) {
    try {
      model.addAttribute("questionForm", questionFormService.findById(id));
      return "questionform/create";
    } catch (BelongToAnotherUserException e) {
      model.addAttribute("error", e.getMessage());
    } catch (QuestionFormNotFoundException e) {
      model.addAttribute("error", e.getMessage());
    } catch (MissingUserException e) {
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "questionform/list";
  }

  @GetMapping("/finish/{questionFormId}")
  public String finishQuestionForm(@PathVariable long questionFormId, Model model) {
    try {
      model.addAttribute("questionFormId", questionFormId);
      questionFormService.finishQuestionForm(questionFormId);
    } catch (MissingUserException e) {
      model.addAttribute("error", e.getMessage());
    } catch (QuestionFormNotFoundException e) {
      model.addAttribute("error", e.getMessage());
    } catch (BelongToAnotherUserException e) {
      model.addAttribute("error", e.getMessage());
    } catch (NotEnoughQuestionsToCreateFormException e) {
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "question/create";
  }

  @GetMapping("/list-questions/{questionFormId}")
  public String listAllQuestionsBelongingToQuestionFormById(@RequestParam(required = false) String error, @PathVariable long questionFormId, Model model) {
    try {
      model.addAttribute("questions", questionFormService.findByIdAndAddQuestionType(questionFormId) );
      model.addAttribute("error", error);
      return "questionform/list-questions";
    } catch (MissingUserException e) {
      model.addAttribute("error", e.getMessage());
    } catch (BelongToAnotherUserException e) {
      model.addAttribute("error", e.getMessage());
    } catch (QuestionFormNotFoundException e) {
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "questionform/list";
  }
}
