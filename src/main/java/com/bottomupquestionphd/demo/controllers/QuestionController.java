package com.bottomupquestionphd.demo.controllers;

import com.bottomupquestionphd.demo.domains.dtos.question.QuestionCreateDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.TextQuestionDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.services.questions.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("question")
public class QuestionController {
  private final QuestionService questionService;

  public QuestionController(QuestionService questionService) {
    this.questionService = questionService;
  }

  @GetMapping("/create-question/{questionFormId}")
  public String renderCreateQuestion(@PathVariable long questionFormId, Model model){
    model.addAttribute("questionCreateDTO", new QuestionCreateDTO());
    model.addAttribute("questionFormId", questionFormId);
    return "question/create-question";
  }

  @PostMapping("/create-question/{questionFormId}")
  public String saveQuestion(@PathVariable long questionFormId, @ModelAttribute QuestionCreateDTO questionCreateDTO, Model model){
    model.addAttribute("questionCreateDTO", questionCreateDTO);
    return "question/create-question";
  }

  @PostMapping("/create/{type}/{questionFormId}")
  public String saveQuestion(Model model, @ModelAttribute TextQuestionDTO textQuestionDTO, @PathVariable String type, @PathVariable long questionFormId){
   model.addAttribute("textQuestionDTO", textQuestionDTO);
    try{
      questionService.saveQuestion(type, textQuestionDTO,questionFormId);
      return "redirect:/";
    }catch (MissingParamsException e){
      model.addAttribute("error", e.getMessage());
    }catch (QuestionFormNotFoundException e){
      model.addAttribute("error", e.getMessage());
    }
    return "question/create-question";
  }
}
