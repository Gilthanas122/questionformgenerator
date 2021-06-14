package com.bottomupquestionphd.demo.controllers.answers;

import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.answerformfilter.SearchTermsForFilteringDTO;
import com.bottomupquestionphd.demo.services.answerformfilter.AnswerFormFilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    log.info("POST answer-form-filter/search started");
    try {
      log.info("POST answer-form-filter/search finished");
      model.addAttribute("answerResult", answerFormFilterService.filterAnswers(questionFormId, searchTermsForFilteringDTO));
      return "answerform-filter/result";
    }catch (Exception e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

/*  @PostMapping("search/{questionFormId}")
  @ResponseBody
  public AnswerSearchTermResultDTO postFilterAnswers(@PathVariable long questionFormId, @ModelAttribute SearchTermsForFilteringDTO searchTermsForFilteringDTO, Model model){
    log.info("POST answer-form-filter/search started");
    try {
      log.info("POST answer-form-filter/search finished");
      return answerFormFilterService.filterAnswers(questionFormId, searchTermsForFilteringDTO);
    }catch (Exception e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return null;
  }*/
}
