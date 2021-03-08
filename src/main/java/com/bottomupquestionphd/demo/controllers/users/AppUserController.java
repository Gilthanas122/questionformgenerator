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


    @GetMapping("question-form/list/{appUserId}")
    public String getQuestionFormsBelongingToUserByUserId(@PathVariable long appUserId, Model model){
        try {
           model.addAttribute("questionFormDTOs", appUserContentService.findQuestionFormBelongingToUser(appUserId));
           return "app-user/list-questionforms";
        }catch (NoSuchUserByIdException e){
            model.addAttribute("error", e.getMessage());
        }catch (BelongToAnotherUserException e){
            model.addAttribute("error", e.getMessage());
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
