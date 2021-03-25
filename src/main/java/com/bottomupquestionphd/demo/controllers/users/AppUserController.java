package com.bottomupquestionphd.demo.controllers.users;

import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.services.appuser.AppUserContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(AppUserController.class);
    private final AppUserContentService appUserContentService;

    public AppUserController(AppUserContentService appUserContentService) {
        this.appUserContentService = appUserContentService;
    }

    @GetMapping("landing-page")
    public String renderLandingPage() {
        log.info("GET /landing-page started");
        log.info("GET /landing-page finished");
        return "app-user/landing-page";
    }

    @GetMapping("question-form/list")
    public String getQuestionFormsFilledOutUserByUserIdFromNavBarMenu(Model model) {
        log.info("GET /question-form/list started");
        try {
            long appUserId = appUserContentService.findCurrentlyLoggedInUsersId();
            model.addAttribute("questionFormDTOs", appUserContentService.findQuestionFormsFilledOutByUser(appUserId));
            log.info("GET /question-form/list finished");
            return "app-user/list-filled-out-questionforms";
        } catch (NoSuchUserByIdException e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
        } catch (BelongToAnotherUserException e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
        }
        return "app-user/landing-page";
    }


    @GetMapping("question-form/list/{appUserId}")
    public String getQuestionFormsFilledOutUserByUserId(@PathVariable long appUserId, Model model) {
        log.info("GET /question-form/list/" + appUserId + " started");
        try {
            model.addAttribute("questionFormDTOs", appUserContentService.findQuestionFormsFilledOutByUser(appUserId));
            log.info("GET /question-form/list/" + appUserId + " finished");
            return "app-user/list-filled-out-questionforms";
        } catch (NoSuchUserByIdException e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
        } catch (BelongToAnotherUserException e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
        }
        return "app-user/landing-page";
    }

    @GetMapping("/question-form/list-not-filled-out")
    public String returnQuestionFormsNotFilledOutByUser(Model model) {
        log.info("GET /question-form/list-not-filled-out/" +" started");
        try {
            model.addAttribute("questionForms", appUserContentService.findAllQuestionFormsNotFilledOutByUser());
            log.info("GET /question-form/list-not-filled-out/" + " finished");
            return "app-user/list-not-filled-out-question-forms";
        } catch (BelongToAnotherUserException e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
        }
        return "app-user/landing-page";
    }

/*    @GetMapping("/question-form/list-not-filled-out/{appUserId}")
    public String returnQuestionFormsNotFilledOutByUserFromNavbar(Model model, long appUserId) {
        log.info("GET /question-form/list-not-filled-out/" + appUserId + " started");
        try {
            model.addAttribute("questionForms", appUserContentService.findAllQuestionFormsNotFilledOutByUser(appUserId));
            log.info("GET /question-form/list-not-filled-out/" + appUserId + " finished");
            return "app-user/list-not-filled-out-question-forms";
        } catch (BelongToAnotherUserException e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            model.addAttribute("error", e.getMessage());
        }
        return "app-user/landing-page";
    }*/
}
