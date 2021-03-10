package com.bottomupquestionphd.demo.controllers.answers;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.services.answerforms.AnswerFormService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("answer-form")
public class AnswerFormController {
    private final AnswerFormService answerFormService;

    public AnswerFormController(AnswerFormService answerFormService) {
        this.answerFormService = answerFormService;
    }

    @GetMapping("create/{questionFormId}")
    public String renderCreateAnswerForm(Model model, @PathVariable long questionFormId) {
        try {
                model.addAttribute("answerForm", answerFormService.createFirstAnswerForm(questionFormId));
        } catch (MissingUserException e) {
            model.addAttribute("error", e.getMessage());
        } catch (QuestionFormNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            e.printStackTrace();
        }

        return "answerform/create";
    }

    @PostMapping("create/{answerFormId}/{questionFormId}/{appUserId}")
    public String submitAnswerForm(RedirectAttributes redirectAttributes, @ModelAttribute AnswerForm answerForm, @PathVariable long answerFormId, @PathVariable long questionFormId, @PathVariable long appUserId) {
        try {
            answerFormService.saveAnswerForm(answerForm, answerFormId, questionFormId, appUserId);
            return "redirect:/question-form/list/" + appUserId;
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
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        redirectAttributes.addFlashAttribute("answerForm", answerForm);
        return "redirect:/answer-form/create/" + questionFormId;
    }

}
