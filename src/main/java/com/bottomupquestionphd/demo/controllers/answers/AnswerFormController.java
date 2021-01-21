package com.bottomupquestionphd.demo.controllers.answers;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("answer-form")
public class AnswerFormController {

  @GetMapping("create")
  public String renderCreateAnswerForm(Model model){
    model.addAttribute("answerForm", new AnswerForm());
    return "answerform/create";
  }
}
