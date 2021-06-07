package com.bottomupquestionphd.demo.controllers.answers;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.answerform.AnswerFormAlreadyFilledOutByCurrentUserException;
import com.bottomupquestionphd.demo.exceptions.answerform.AnswerFormNotFilledOutException;
import com.bottomupquestionphd.demo.exceptions.answerform.NoSuchAnswerformById;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.services.answerforms.AnswerFormService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("answer-form")
@PreAuthorize("isAuthenticated()")
public class AnswerFormController {
  private static final Logger log = LoggerFactory.getLogger(AnswerFormController.class);
  private final AnswerFormService answerFormService;

  public AnswerFormController(AnswerFormService answerFormService) {
    this.answerFormService = answerFormService;
  }

  @GetMapping("create/{questionFormId}")
  public String renderCreateAnswerForm(Model model, @PathVariable long questionFormId) {
    log.info("GET answer-form/create/" + questionFormId + "started");
    try {
      model.addAttribute("answerForm", answerFormService.createAnswerFormDTO(questionFormId));
      log.info("GET answer-form/create/" + questionFormId + "finished");
      return "answerform/create";
    } catch (MissingUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (QuestionFormNotFoundException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (AnswerFormAlreadyFilledOutByCurrentUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/app-user/question-form/list";
  }

  @PostMapping("create/{questionFormId}/{appUserId}")
  public String submitAnswerForm(RedirectAttributes redirectAttributes, @ModelAttribute AnswerForm answerForm, @PathVariable long questionFormId, @PathVariable long appUserId, Model model) {
    log.info("POST answer-form/create/" + "/" + questionFormId + "/" + appUserId + " started");
    try {
      answerFormService.saveAnswerForm(answerForm, questionFormId, appUserId);
      model.addAttribute("successMessage", "Question formed successfully filled out");
      log.info("POST answer-form/create/" + "/" + questionFormId + "/" + appUserId + " finished");
      return "redirect:/app-user/landing-page/";
    } catch (MissingUserException e) {
      log.error(e.getMessage());
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    } catch (NoSuchUserByIdException e) {
      log.error(e.getMessage());
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    } catch (QuestionFormNotFoundException e) {
      log.error(e.getMessage());
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    } catch (BelongToAnotherUserException e) {
      log.error(e.getMessage());
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    } catch (MissingParamsException e) {
      log.error(e.getMessage());
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    } catch (AnswerFormAlreadyFilledOutByCurrentUserException e) {
      log.error(e.getMessage());
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    }
    redirectAttributes.addFlashAttribute("answerForm", answerForm);
    return "redirect:/answer-form/create/" + questionFormId;
  }

  @GetMapping("/update/{questionFormId}/{answerFormId}/{appUserId}")
  public String updateAnswerFormCreatedByUser(@PathVariable long questionFormId, @PathVariable long answerFormId, @PathVariable long appUserId, Model model) {
    log.info("GET answer-form/update/" + questionFormId + "/" + answerFormId + "/" + appUserId + " started");
    try {
      model.addAttribute("answerForm", answerFormService.createAnswerFormToUpdate(questionFormId, answerFormId, appUserId));
      log.info("GET answer-form/update/" + questionFormId + "/" + answerFormId + "/" + appUserId + " finished");
      return "answerform/update";
    } catch (BelongToAnotherUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (QuestionFormNotFoundException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (MissingUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (AnswerFormNotFilledOutException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
      e.printStackTrace();
    }
    return "app-user/landing-page";
  }

  @PostMapping("/update/{answerFormId}/{appUserId}")
  public String updateAnswerFormWithNewAnswers(@PathVariable long appUserId, @PathVariable long answerFormId,
                                               @ModelAttribute AnswerForm answerForm, Model model) {
    log.info("POST /update/" + answerFormId + "/" + appUserId + " started");
    try {
      answerFormService.saveUpdatedAnswerForm(answerFormId, appUserId, answerForm);
      model.addAttribute("successMessage", "AnswerForm successfully updated");
    } catch (NoSuchAnswerformById e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (BelongToAnotherUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (NoSuchUserByIdException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    log.info("POST /update/" + answerFormId + "/" + appUserId + " finished");
    return "app-user/landing-page";
  }
}
