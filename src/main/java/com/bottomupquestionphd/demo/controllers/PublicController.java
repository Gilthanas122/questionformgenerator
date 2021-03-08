package com.bottomupquestionphd.demo.controllers;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.dtos.appuser.LoginDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class PublicController {

  private final AppUserService appUserService;

  public PublicController(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @GetMapping("/")
  public String renderIndex(){
    return "index";
  }

  @GetMapping("register")
  public String renderAppUserCreateForm(Model model){
    model.addAttribute("error", null);
    model.addAttribute("appUser", new AppUser());
    return "register";
  }

  @PostMapping("register")
  public String saveUser(@ModelAttribute AppUser appUser, Model model){
    model.addAttribute("appUser", appUser);
     try {
       appUserService.saveUser(appUser);
       return "redirect:/login";
     }catch (MissingParamsException e){
       model.addAttribute("error", e.getMessage());
     }catch (UsernameAlreadyTakenException e) {
       model.addAttribute("error", e.getMessage());
     }catch (PasswordNotComplexEnoughException e){
       model.addAttribute("error", e.getMessage());
     } catch (Exception e){
       model.addAttribute("error", e.getMessage());
     }
    return "register";
  }

  @GetMapping("login")
  public String renderLogin(Model model, @RequestParam (required = false) String error){
    model.addAttribute("error", error);
    model.addAttribute("loginDTO", new LoginDTO());
    return "login";
  }


  @GetMapping("/login-error")
  public String login(HttpServletRequest request, Model model){
    try {
      validateLoginCredentials(request);
    }catch (BadCredentialsException e){
      model.addAttribute("error", e.getMessage());
    }catch (Exception e){
      model.addAttribute("error", e.getMessage());
    }
    return "login";
  }

  private void validateLoginCredentials(HttpServletRequest request){
    HttpSession session = request.getSession(false);
    if (session != null) {
      AuthenticationException ex = (AuthenticationException) session
              .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
      if (ex != null) {
        throw new BadCredentialsException("Bad username or password");
      }
    }
  }
}
