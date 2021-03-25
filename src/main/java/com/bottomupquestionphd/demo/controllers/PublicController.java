package com.bottomupquestionphd.demo.controllers;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.dtos.appuser.LoginDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.InvalidRegexParameterException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByEmailException;
import com.bottomupquestionphd.demo.exceptions.appuser.UsernameAlreadyTakenException;
import com.bottomupquestionphd.demo.exceptions.email.ConfirmationTokenDoesNotExistException;
import com.bottomupquestionphd.demo.exceptions.email.EmailAlreadyUsedException;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PublicController {
  private static final Logger log = LoggerFactory.getLogger(PublicController.class);
  private final AppUserService appUserService;

  public PublicController(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  @GetMapping("/")
  public String renderIndex() {
    log.info("GET / started");
    log.info("GET / finished");
    return "index";
  }

  @GetMapping("register")
  public String renderAppUserCreateForm(Model model) {
    log.info("GET /register started");
    model.addAttribute("error", null);
    model.addAttribute("appUser", new AppUser());
    log.info("GET /register finished");
    return "register";
  }

  @PostMapping("register")
  public String saveUser(@ModelAttribute AppUser appUser, Model model) {
    log.info("POST /register started");
    model.addAttribute("appUser", appUser);
    try {
      appUserService.saveUser(appUser);
      log.info("POST /register finished");
      return "redirect:/login";
    } catch (MissingParamsException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (UsernameAlreadyTakenException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (InvalidRegexParameterException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (EmailAlreadyUsedException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "register";
  }

  @GetMapping("login")
  public String renderLogin(Model model, @RequestParam(required = false) String error) {
    log.info("GET /login started");
    model.addAttribute("error", error);
    model.addAttribute("loginDTO", new LoginDTO());
    log.info("GET /login finished");
    return "login";
  }

  @GetMapping("verify-account")
  public String verifyUserAccount(@RequestParam String token, Model model) {
    log.info("GET /verify-account started");
    try {
      model.addAttribute("successMessage", appUserService.activateUserByEmail(token));
      log.info("GET /verify-account finished");
      return "redirect:/login";
    } catch (ConfirmationTokenDoesNotExistException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (NoSuchUserByEmailException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "register";
  }
}
