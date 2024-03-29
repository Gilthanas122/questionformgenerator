package com.bottomupquestionphd.demo.controllers.vote;

import com.bottomupquestionphd.demo.controllers.answers.AnswerFormController;
import com.bottomupquestionphd.demo.domains.dtos.textanswervote.ReceiveTextAnswerVotesDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.answerform.AnswerFormNotFilledOutException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.exceptions.textanswervote.NoActualAnswerTextsToVoteForException;
import com.bottomupquestionphd.demo.services.answers.textanswervotes.TextAnswerVoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("text-answer-vote")
@PreAuthorize("isAuthenticated()")
public class TextAnswerVoteController {
  private static final Logger log = LoggerFactory.getLogger(AnswerFormController.class);
  private final TextAnswerVoteService textAnswerVoteService;

  public TextAnswerVoteController(TextAnswerVoteService textAnswerVoteService) {
    this.textAnswerVoteService = textAnswerVoteService;
  }

  // NOT TESTED
  @GetMapping("create/{appUserid}/{questionFormId}/{answerFormId}")
  public String renderOtherUsersAnswers(@PathVariable long appUserid, @PathVariable long questionFormId, @PathVariable long answerFormId, Model model) {
    log.info("GET answer-form/vote/ " + appUserid + "/" + questionFormId + "/" + appUserid + " started");
    try {
      model.addAttribute("answersTextsNotVotedByUser", textAnswerVoteService.returnAnswersNotVotedByCurrentUser(appUserid, questionFormId, answerFormId));
      model.addAttribute("receiveTextAnswerVotesDTO", new ReceiveTextAnswerVotesDTO());
      log.info("GET answer-form/vote/ " + appUserid + "/" + questionFormId + "/" + appUserid + " finished");
      return "answerform/vote";
    } catch (BelongToAnotherUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (MissingUserException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (QuestionFormNotFoundException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (AnswerFormNotFilledOutException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (MissingParamsException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (NoActualAnswerTextsToVoteForException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  //NOT TESTED
  @PostMapping("create/{appUserid}/{questionFormId}/{answerFormId}")
  public String saveOtherUsersAnswers(@ModelAttribute ReceiveTextAnswerVotesDTO receiveTextAnswerVotesDTO, @PathVariable long appUserid, @PathVariable long questionFormId, @PathVariable long answerFormId, Model model) {
    log.info("POST answer-form/vote/ " + appUserid + "/" + questionFormId + "/" + appUserid + " started");
    try {
      textAnswerVoteService.saveVotes(receiveTextAnswerVotesDTO, appUserid, questionFormId, answerFormId);
      log.info("POST answer-form/vote/ " + appUserid + "/" + questionFormId + "/" + appUserid + " finished");
      return "app-user/landing-page";
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }
}
