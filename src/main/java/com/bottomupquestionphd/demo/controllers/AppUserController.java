package com.bottomupquestionphd.demo.controllers;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.dtos.AppUserTokenDTO;
import com.bottomupquestionphd.demo.domains.dtos.LoginDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AppUserController {

  private final AppUserService appUserService;

  public AppUserController(AppUserService appUserService) {
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
  public String renderLogin(Model model){
    model.addAttribute("error", null);
    model.addAttribute("loginDTO", new LoginDTO());
    return "login";
  }

  @PostMapping("login")
  public String validateLogin(@ModelAttribute LoginDTO loginDTO, Model model){
    try {
      model.addAttribute("loginDTO", loginDTO);
      AppUserTokenDTO appUserTokenDTO = appUserService.validateLogin(loginDTO);
      model.addAttribute("appUserTokenDTO", appUserTokenDTO);
      return "redirect:/landing-page";
    }catch (InvalidLoginException e){
      model.addAttribute("error", e.getMessage());
    } catch (NoSuchUserNameException e) {
      model.addAttribute("error", e.getMessage());
    } catch (AppUserPasswordMissMatchException e) {
      model.addAttribute("error", e.getMessage());
    } catch (Exception e){
      model.addAttribute("error", e.getMessage());
    }
    return "login";
  }
  
  @GetMapping("landing-page")
  public String renderLandingPage( Model model){
    return "landing-page";
  }

}
