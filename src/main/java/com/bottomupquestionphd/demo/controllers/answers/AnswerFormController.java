package com.bottomupquestionphd.demo.controllers.answers;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.services.answerforms.AnswerFormService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("answer-form")
public class AnswerFormController {
  private AnswerFormService answerFormService;

  public AnswerFormController(AnswerFormService answerFormService) {
    this.answerFormService = answerFormService;
  }

  @GetMapping("create/{questionFormId}")
  public String renderCreateAnswerForm(Model model, @PathVariable long questionFormId){
    try {
      model.addAttribute("answerForm", answerFormService.createFirstAnswerForm(questionFormId));
    }catch (MissingUserException e){
      model.addAttribute("error", e.getMessage());
    }catch (QuestionFormNotFoundException e){
      model.addAttribute("error", e.getMessage());
    }catch (Exception e){
      model.addAttribute("error", e.getMessage());
    }

    return "answerform/create";
  }

  @PostMapping("submit/{answerFormId}")
  @ResponseBody
  public String submitAnswerForm(@ModelAttribute AnswerForm answerForm){
    answerFormService.saveAnswerForm(answerForm);
    return "success";
  }
}
