package com.bottomupquestionphd.demo.controllers.users;

import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.services.appuser.AppUserContentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("app-user")
public class AppUserController {
    private final AppUserContentService appUserContentService;

    public AppUserController(AppUserContentService appUserContentService) {
        this.appUserContentService = appUserContentService;
    }

    @GetMapping("landing-page")
    public String renderLandingPage() {
        return "app-user/landing-page";
    }

    @GetMapping("question-form/list")
    public String getQuestionFormsFilledOutUserByUserIdFromNavBarMenu(Model model) {
        try {
            long appUserId = appUserContentService.findCurrentlyLoggedInUsersId();
            model.addAttribute("questionFormDTOs", appUserContentService.findQuestionFormsFilledOutByUser(appUserId));
            return "app-user/list-filled-out-questionforms";
        } catch (NoSuchUserByIdException e) {
            model.addAttribute("error", e.getMessage());
        } catch (BelongToAnotherUserException e) {
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "app-user/landing-page";
    }


    @GetMapping("question-form/list/{appUserId}")
    public String getQuestionFormsFilledOutUserByUserId(@PathVariable long appUserId, Model model) {
        try {
            if (appUserId == 0) {
                appUserId = appUserContentService.findCurrentlyLoggedInUsersId();
            }
            model.addAttribute("questionFormDTOs", appUserContentService.findQuestionFormsFilledOutByUser(appUserId));
            return "app-user/list-filled-out-questionforms";
        } catch (NoSuchUserByIdException e) {
            model.addAttribute("error", e.getMessage());
        } catch (BelongToAnotherUserException e) {
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "app-user/landing-page";
    }

    @GetMapping("/question-form/list-not-filled-out/{appUserId}")
    public String returnQuestionFormsNotFilledOutByUser(@PathVariable long appUserId, Model model) {
        try {
            if (appUserId == 0) {
                appUserId = appUserContentService.findCurrentlyLoggedInUsersId();
            }
            model.addAttribute("questionForms", appUserContentService.findAllQuestionFormsNotFilledOutByUser(appUserId));
            return "app-user/list-not-filled-out-question-forms";
        } catch (BelongToAnotherUserException e) {
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "app-user/landing-page";
    }

    @GetMapping("/question-form/list-not-filled-out")
    public String returnQuestionFormsNotFilledOutByUserFromNavbar(Model model) {
        try {
            long appUserId = appUserContentService.findCurrentlyLoggedInUsersId();
            model.addAttribute("questionForms", appUserContentService.findAllQuestionFormsNotFilledOutByUser(appUserId));
            return "app-user/list-not-filled-out-question-forms";
        } catch (BelongToAnotherUserException e) {
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "app-user/landing-page";
    }
}
