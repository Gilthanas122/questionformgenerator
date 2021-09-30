package com.bottomupquestionphd.demo.controllers.answers;

import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.answerformfilter.SearchTermsForFilteringDTO;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionFormFilteringException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.services.answerformfilter.AnswerFormFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("answer-form/filter")
@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TEACHER')")
public class AnswerFormFilterController {
  private static final Logger log = LoggerFactory.getLogger(AnswerFormController.class);
  private AnswerFormFilterService answerFormFilterService;

  public AnswerFormFilterController(AnswerFormFilterService answerFormFilterService) {
    this.answerFormFilterService = answerFormFilterService;
  }

  @GetMapping("search/{questionFormId}")
  public String renderSearchFormForQuestions(@PathVariable long questionFormId, Model model){
    log.info("GET answer-form-filter/search started");
    try{
      QuestionForm questionForm = answerFormFilterService.generateSearchFields(questionFormId);
      model.addAttribute("questionForm", questionForm);
      log.info("GET answer-form-filter/search finished");
      return "answerform-filter/search";
    }catch (Exception e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @PostMapping("search/{questionFormId}")
  public String postFilterAnswers(@PathVariable long questionFormId, @ModelAttribute SearchTermsForFilteringDTO searchTermsForFilteringDTO, Model model){
    log.info("POST answer-form/filter/search started");
    try {
      log.info("POST answer-form/filter/search finished");
      model.addAttribute("answerResult", answerFormFilterService.filterAnswers(questionFormId, searchTermsForFilteringDTO));
      return "answerform-filter/result";
    }catch (MissingUserException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (QuestionFormNotFoundException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (BelongToAnotherUserException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (QuestionFormFilteringException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (Exception e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @GetMapping("/download-all-answers/{questionFormId}")
  public String returnAllAnswers(@PathVariable long questionFormId, Model model, HttpServletResponse response){
    log.info("GET answer-form/filter/return-all-answers/" + questionFormId + " started");
    try {
      log.info("GET answer-form/filter/return-all-answers/" + questionFormId + " finished");
      answerFormFilterService.returnAllAnswersBelongingToQuestionForm(questionFormId,response);
    }catch (MissingUserException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (BelongToAnotherUserException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (QuestionFormNotFoundException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (Exception e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/questionform/list";
  }
}
