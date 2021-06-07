package com.bottomupquestionphd.demo.controllers;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.dtos.SuccessMessageDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUserLoginDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.ChangePasswordDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.exceptions.email.EmailAlreadyUsedException;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    model.addAttribute("appUser", new AppUser.Builder().build());
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
    model.addAttribute("loginDTO", new AppUserLoginDTO());
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
    } catch (NoSuchUserByEmailException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (AppUserIsAlreadyActivatedException e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "redirect:/login";
  }

  @GetMapping("/change-password")
  public String getChangeUserPassword(){
    log.info("GET /change-password started");
    log.info("GET /change-password finished");
    return "change-password";
  }

  @PostMapping("/change-password")
  public String postChangeUserPassword(Model model, @RequestParam String email){
    log.info("POST /change-password started");
    try {
      appUserService.sendEmailToRegeneratePassword(email);
      log.info("POST /change-password finished");
      model.addAttribute("successMessage", new SuccessMessageDTO("ok", ""));
      return "login";
    }catch (MissingParamsException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (AppUserNotActivatedException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (NoSuchUserByEmailException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (InvalidRegexParameterException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (Exception e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "change-password";
  }

  @GetMapping("reset-password/{appUserId}")
  public String getResetPassword(@PathVariable long appUserId, Model model, @RequestParam String token){
    log.info("GET /reset-password started");
    try {
      appUserService.validateChangePassword(appUserId, token);
      model.addAttribute("appUserId", appUserId);
      log.info("GET /reset-password finished");
      return "reset-password";
    }catch (NoSuchUserByIdException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (InvalidChangePasswordException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (Exception e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "index";
  }

  @PostMapping("reset-password/{appUserId}")
  public String postResetPassword(Model model, @ModelAttribute ChangePasswordDTO changePasswordDTO, @PathVariable long appUserId){
    log.info("POST /reset-password started");
    try {
      appUserService.changePassword(changePasswordDTO, appUserId);
      log.info("POST /reset-password finished");
      return "login";
    }catch (NoSuchUserByIdException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (PassWordMissMachException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (InvalidRegexParameterException e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }catch (Exception e){
      log.error(e.getMessage());
      model.addAttribute("error", e.getMessage());
    }
    return "reset-password";
  }
}
