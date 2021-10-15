package com.bottomupquestionphd.demo.controllers.answers;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.answer.AnswerNotFoundByIdException;
import com.bottomupquestionphd.demo.exceptions.answerform.*;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionTypesAndQuestionTextsSizeMissMatchException;
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

  // SHOULD RE-TEST
  @PostMapping("create/{questionFormId}/{appUserId}")
  public String submitAnswerForm(RedirectAttributes redirectAttributes, @ModelAttribute AnswerForm answerForm, @PathVariable long questionFormId, @PathVariable long appUserId, Model model) {
    log.info("POST answer-form/create/" + "/" + questionFormId + "/" + appUserId + " started");
    try {
      AnswerForm answerFormReturned = answerFormService.saveAnswerForm(answerForm, questionFormId, appUserId);
      model.addAttribute("successMessage", "Question formed successfully filled out");
      log.info("POST answer-form/create/" + "/" + questionFormId + "/" + appUserId + " finished");
      return "redirect:/text-answer-vote/create/" + appUserId + "/" + questionFormId + "/" + answerFormReturned.getId();
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

  // RE-TEST
  @GetMapping("/update/{questionFormId}/{appUserId}")
  public String updateAnswerFormCreatedByUser(@PathVariable long questionFormId, @PathVariable long appUserId, Model model) {
    log.info("GET answer-form/update/" + questionFormId + "/" + appUserId + " started");
    try {
      model.addAttribute("answerForm", answerFormService.createAnswerFormToUpdate(questionFormId, appUserId));
      log.info("GET answer-form/update/" + questionFormId + "/" + appUserId + " finished");
      return "answerform/update";
    } catch (BelongToAnotherUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (QuestionFormNotFoundException e) {
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
    log.info("POST answer-form/update/" + answerFormId + "/" + appUserId + " started");
    try {
      AnswerForm answerFormReturned = answerFormService.saveUpdatedAnswerForm(answerFormId, appUserId, answerForm);
      model.addAttribute("successMessage", "AnswerForm successfully updated");
      log.info("POST answer-form/update/" + answerFormId + "/" + appUserId + " finished");
      return "redirect:/text-answer-vote/create/" + answerFormReturned.getAppUser().getId() + "/" + answerFormReturned.getQuestionForm().getId() + "/" + answerFormReturned.getId();
    } catch (NoSuchAnswerformById e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (BelongToAnotherUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (NoSuchUserByIdException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (NumberOfQuestionAndAnswersShouldMatchException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (AnswerFormNumberOfAnswersShouldMatchException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "app-user/landing-page";
  }

  @GetMapping("/get/{answerId}")
  public String getAnswerFormBelongingToAnswer(@PathVariable long answerId, Model model) {
    log.info("GET answer-form/get/ " + answerId + " started");
    try {
      model.addAttribute("displayAnswers", answerFormService.findAnswerFormByAnswerId(answerId));
      log.info("GET answer-form/get/ " + answerId + " finished");
      return "answerform/display-answers";
    } catch (BelongToAnotherUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (AnswerNotFoundByIdException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (NoSuchAnswerformById e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "app-user/landing-page";
  }

  @GetMapping("/answers/{questionFormId}/{appUserId}")
  public String showAllAnswersBelongingToQuestionForm(@PathVariable long questionFormId, @PathVariable long appUserId, Model model) {
    log.info("GET answer-form/answers/ " + questionFormId + " started");
    try {
      log.info("GET answer-form/answers/ " + questionFormId + " finished");
      model.addAttribute("displayAnswersFromAnAnswerFormDTO", answerFormService.findAllAnswersBelongingToQuestionForm(questionFormId, appUserId));
      return "answerform/user-answers";
    } catch (AnswerFormNotFilledOutException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (MissingUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (QuestionFormNotFoundException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (BelongToAnotherUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (QuestionTypesAndQuestionTextsSizeMissMatchException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @GetMapping("list-answers/{questionFormId}")
  public String seeUsersAnswersBelongingToAQuestionForm(@PathVariable long questionFormId, Model model) {
    log.info("GET answer-form/list-answers/ " + questionFormId  + " started");
    try {
      model.addAttribute("displayOneUserAnswersDTO", answerFormService.findAllAnswersBelongingToAnUser(questionFormId));
      log.info("GET answer-form/list-answers/ " + questionFormId + " finished");
      return "answerform/list-all-user-answers";
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "app-user/landing-page";
  }
}
