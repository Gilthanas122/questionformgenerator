package com.bottomupquestionphd.demo.controllers;

import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormCreateDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserNameException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNameAlreadyExistsException;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasRole('ROLE_TEACHER')")
@RequestMapping("question-form")
public class QuestionFormController {
  private final QuestionFormService questionFormService;

  public QuestionFormController(QuestionFormService questionFormService) {
    this.questionFormService = questionFormService;
  }

  @GetMapping("create")
  public String renderCreateQuestionForm(Model model){
    model.addAttribute("questionFormDTO", new QuestionFormCreateDTO());
    return "questionform/create-question-form";
  }

  @PostMapping("create")
  public String saveQuestionForm(@ModelAttribute QuestionFormCreateDTO questionFormCreateDTO, Model model){
    model.addAttribute("questionFormDTO", questionFormCreateDTO);
    try {
      long questionFormId = questionFormService.save(questionFormCreateDTO);
      return "redirect:/question/create/" + questionFormId;
    }catch (MissingParamsException e){
      model.addAttribute("error", e.getMessage());
    }catch (QuestionFormNameAlreadyExistsException e){
      model.addAttribute("error", e.getMessage());
    } catch (NoSuchUserNameException e) {
      model.addAttribute("error", e.getMessage());
    } catch (Exception e){
      model.addAttribute("error", e.getMessage());
    }
    return "questionform/create";
  }

  @GetMapping("list")
  public String listTeachersQuestionForms(Model model){
    try{
      model.addAttribute(questionFormService.findAll());
    }
  }
}
