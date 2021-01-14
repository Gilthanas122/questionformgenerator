package com.bottomupquestionphd.demo.controllers;

import com.bottomupquestionphd.demo.domains.dtos.question.QuestionCreateDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionChangeException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionException;
import com.bottomupquestionphd.demo.exceptions.question.QuestionNotFoundByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.services.questions.QuestionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@PreAuthorize("hasRole('ROLE_TEACHER')")
@RequestMapping("question")
public class QuestionController {
  private final QuestionService questionService;

  public QuestionController(QuestionService questionService) {
    this.questionService = questionService;
  }

  @GetMapping("/create/{questionFormId}")
  public String renderCreateQuestion(@PathVariable long questionFormId, Model model) {
    model.addAttribute("questionFormId", questionFormId);
    return "question/create";
  }

  @PostMapping("/create/{type}/{questionFormId}")
  public String saveQuestion(Model model, @ModelAttribute QuestionCreateDTO questionDTO, @PathVariable String type, @PathVariable long questionFormId) {
    model.addAttribute("questionDTO", questionDTO);
    try {
      questionService.saveQuestion(type, questionDTO, questionFormId);
      return "redirect:/question/create/" + questionFormId;
    } catch (MissingParamsException e) {
      model.addAttribute("error", e.getMessage());
    } catch (QuestionFormNotFoundException e) {
      model.addAttribute("error", e.getMessage());
    } catch (BelongToAnotherUserException e) {
      model.addAttribute("error", e.getMessage());
    } catch (MissingUserException e) {
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "question/create";
  }

  @GetMapping("update/{questionId}")
  public String renderQuestionUpdate(@PathVariable long questionId, Model model) {
    try {
      QuestionWithDTypeDTO question = questionService.findByIdAndConvertToQuestionWithDTypeDTO(questionId);
      model.addAttribute("question", question);
      return "question/update";
    } catch (QuestionNotFoundByIdException e) {
      model.addAttribute("error", e.getMessage());
    } catch (BelongToAnotherUserException e) {
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/question-form/list";
  }

  @PostMapping("update/{questionId}")
  public String updateQuestionById(@ModelAttribute QuestionWithDTypeDTO question, Model model, @PathVariable long questionId) {
    try {
      questionService.saveQuestionFromQuestionDType(question);
      return "redirect:/question-form/list-questions/" + questionService.findQuestionFormIdBelongingToQuestion(questionId);
    } catch (Exception e) {
      e.printStackTrace();
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/question/update/" + questionId;
  }

  @GetMapping("/update-position/{change}/{questionId}")
  public String updateListPosition(@PathVariable String change, @PathVariable long questionId, RedirectAttributes redirectAttributes) throws QuestionNotFoundByIdException {
    try {
      questionService.changeOrderOfQuestion(change, questionId);
    } catch (InvalidQuestionPositionChangeException e) {
      redirectAttributes.addAttribute("error", e.getMessage());
    } catch (InvalidQuestionPositionException e) {
      redirectAttributes.addAttribute("error", e.getMessage());
    } catch (QuestionNotFoundByIdException e) {
      redirectAttributes.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      redirectAttributes.addAttribute("error", e.getMessage());
    }

    return "redirect:/question-form/list-questions/" + questionService.findQuestionFormIdBelongingToQuestion(questionId) + "?error=";
  }

  @GetMapping("/delete/{questionId}")
  public String deleteQuestionById(@PathVariable long questionId, RedirectAttributes redirectAttributes) throws QuestionNotFoundByIdException {
   long formId = 0;
    try{
      formId = questionService.deleteQuestion(questionId);
      return "redirect:/question-form/list-questions/" +formId;
    }catch (QuestionNotFoundByIdException e){
      redirectAttributes.addAttribute("error", e.getMessage());
    } catch (BelongToAnotherUserException e){
      redirectAttributes.addAttribute("error", e.getMessage());
    }catch (Exception e){
      redirectAttributes.addAttribute("error", e.getMessage());
    }
    return "redirect:/question-form/list/";
  }
}
