package com.bottomupquestionphd.demo.controllers.users;

import com.bottomupquestionphd.demo.services.appuser.AppUserQuestionFormService;
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
    private final AppUserQuestionFormService appUserQuestionFormService;

    public AppUserController(AppUserQuestionFormService appUserQuestionFormService) {
        this.appUserQuestionFormService = appUserQuestionFormService;
    }

    @GetMapping("question-form/list/{appUserId}")
    public String getQuestionFormsBelongingToUserByUserId(@PathVariable long appUserId, Model model){
        try {
           model.addAttribute(appUserQuestionFormService.findQuestionFormBelongingToUser(appUserId));
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
        }
        return "landing-page";
    }

    @GetMapping("landing-page")
    public String renderLandingPage(){
        return "landing-page";
    }
}
