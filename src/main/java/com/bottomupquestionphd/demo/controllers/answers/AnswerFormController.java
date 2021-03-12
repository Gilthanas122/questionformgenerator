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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("answer-form")
@PreAuthorize("isAuthenticated()")
public class AnswerFormController {
    private final AnswerFormService answerFormService;

    public AnswerFormController(AnswerFormService answerFormService) {
        this.answerFormService = answerFormService;
    }

    @GetMapping("create/{questionFormId}")
    public String renderCreateAnswerForm(Model model, @PathVariable long questionFormId) {
        try {
            model.addAttribute("answerForm", answerFormService.createAnswerForm(questionFormId));
            return "answerform/create";
        } catch (MissingUserException e) {
            model.addAttribute("error", e.getMessage());
        } catch (QuestionFormNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (AnswerFormAlreadyFilledOutByCurrentUserException e) {
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/app-user/question-form/list";
    }

    @PostMapping("create/{answerFormId}/{questionFormId}/{appUserId}")
    public String submitAnswerForm(RedirectAttributes redirectAttributes, @ModelAttribute AnswerForm answerForm, @PathVariable long answerFormId, @PathVariable long questionFormId, @PathVariable long appUserId, Model model) {
        try {
            answerFormService.saveAnswerForm(answerForm, answerFormId, questionFormId, appUserId);
            model.addAttribute("successMessage", "Question formed successfully filled out");
            return "redirect:/app-user/landing-page/";
        } catch (MissingUserException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (NoSuchUserByIdException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (QuestionFormNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (BelongToAnotherUserException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (MissingParamsException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (AnswerFormAlreadyFilledOutByCurrentUserException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        redirectAttributes.addFlashAttribute("answerForm", answerForm);
        return "redirect:/answer-form/create/" + questionFormId;
    }

    @GetMapping("/update/{questionFormId}/{answerFormId}/{appUserId}")
    public String updateAnswerFormCreatedByUser(@PathVariable long questionFormId, @PathVariable long answerFormId, @PathVariable long appUserId, Model model) {
        try {
            model.addAttribute("answerForm", answerFormService.createAnswerFormToUpdate(questionFormId, answerFormId, appUserId));
            return "answerform/update";
        } catch (BelongToAnotherUserException e) {
            model.addAttribute("error", e.getMessage());
        } catch (QuestionFormNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (MissingUserException e) {
            model.addAttribute("error", e.getMessage());
        } catch (AnswerFormNotFilledOutException e) {
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "app-user/landing-page";
    }

    @PostMapping("/update/{appUserId}/{answerFormId}")
    public String updateAnswerFormWithNewAnswers(@PathVariable long appUserId, @PathVariable long answerFormId, @ModelAttribute AnswerForm answerForm, Model model){
        try {
            answerFormService.saveUpdatedAnswerForm(answerFormId, appUserId, answerForm);
            model.addAttribute("successMessage", "AnswerForm successfully updated");
        }catch (NoSuchAnswerformById e){
            model.addAttribute("error", e.getMessage());
        }catch (BelongToAnotherUserException e){
            model.addAttribute("error", e.getMessage());
        }catch (NoSuchUserByIdException e){
            model.addAttribute("error", e.getMessage());
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
        }
        return "app-user/landing-page";
    }
}
