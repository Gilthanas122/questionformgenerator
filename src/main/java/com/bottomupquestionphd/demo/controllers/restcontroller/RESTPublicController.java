package com.bottomupquestionphd.demo.controllers.restcontroller;

import com.bottomupquestionphd.demo.controllers.PublicController;
import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.dtos.SuccessMessageDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUserLoginDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUserRegisterDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.ChangePasswordDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.exceptions.email.ConfirmationTokenDoesNotExistException;
import com.bottomupquestionphd.demo.exceptions.email.EmailAlreadyUsedException;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rest")
public class RESTPublicController {

  private static final Logger log = LoggerFactory.getLogger(PublicController.class);

  private final AppUserService appUserService;

  public RESTPublicController(AppUserService appUserService) {
    this.appUserService = appUserService;
  }


  @GetMapping("register")
  public ResponseEntity<AppUser> renderAppUserCreateForm(){
    log.info("REST GET /rest/register started");
    log.info("REST GET /rest/register finished");
    return new ResponseEntity<>(new AppUser.Builder().build(), HttpStatus.OK);
  }

  @PostMapping("register")
  public ResponseEntity<SuccessMessageDTO> saveUser(@RequestBody AppUserRegisterDTO appUserRegisterDTO) throws InvalidRegexParameterException, EmailAlreadyUsedException, MissingParamsException, UsernameAlreadyTakenException {
    log.info("REST POST rest/register started");
    AppUser appUser = new AppUser.Builder().username(appUserRegisterDTO.username).password(appUserRegisterDTO.getPassword()).emailId(appUserRegisterDTO.getEmail()).build();
    appUserService.saveUser(appUser);
    log.info("REST POST rest/register finished");
    return new ResponseEntity(new SuccessMessageDTO("created", "App user successfully registered"), HttpStatus.CREATED);
  }

  @GetMapping("login")
  public ResponseEntity<AppUserLoginDTO> createLoginDTO(){
    log.info("REST GET /rest/login started");
    log.info("REST GET /rest/login finished");
    return new ResponseEntity<>(new AppUserLoginDTO(), HttpStatus.OK);
  }

  @PostMapping("login")
  public ResponseEntity<?> checkLogin(@RequestBody AppUserLoginDTO appUserLoginDTO) throws AppUserNotActivatedException, NoSuchUserNameException, InvalidLoginException, MissingParamsException, AppUserPasswordMissMatchException {
    log.info("REST POST rest/login started");
    appUserService.validateLogin(appUserLoginDTO);
    log.info("REST POST rest/login finished");
    return new ResponseEntity<>(new SuccessMessageDTO("ok","Successful login"), HttpStatus.OK);
  }

  @GetMapping("verify-account")
  public ResponseEntity<SuccessMessageDTO> verifyUserAccount(@RequestParam String token) throws ConfirmationTokenDoesNotExistException, NoSuchUserByEmailException, AppUserIsAlreadyActivatedException, MissingParamsException {
    log.info("REST /GET rest/verify-account started");
    String message = appUserService.activateUserByEmail(token);
    log.info("REST /GET rest/verify-account finished");
    return new ResponseEntity<>(new SuccessMessageDTO("ok", message), HttpStatus.OK);
  }

  @GetMapping("change-password")
  public ResponseEntity<?> getChangeUserPassword(){
    log.info("REST GET rest/change-password started");
    log.info("REST GET /change-password finished");
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("change-password")
  public ResponseEntity<SuccessMessageDTO> postChangeUserPassword(@RequestParam String email) throws AppUserNotActivatedException, InvalidRegexParameterException, NoSuchUserByEmailException, MissingParamsException {
    log.info("REST POST rest /change-password started");
    appUserService.sendEmailToRegeneratePassword(email);
    log.info("REST POST rest/change-password finished");
    return new ResponseEntity<>(new SuccessMessageDTO("ok", "Email sent to the provided email address to change password"), HttpStatus.OK);
  }

  @GetMapping("reset-password/{appUserId}")
  public ResponseEntity<Long> getResetPassword(@PathVariable long appUserId, @RequestParam String token) throws NoSuchUserByIdException, InvalidChangePasswordException, MissingParamsException, BelongToAnotherUserException {
    log.info("REST GET rest/reset-password started");
    appUserService.validateChangePassword(appUserId, token);
    log.info("REST GET rest/reset-password finished");
    return new ResponseEntity<>(appUserId, HttpStatus.OK);
  }

  @PostMapping("reset-password/{appUserId}")
  public ResponseEntity<?> postResetPassword(@RequestBody ChangePasswordDTO changePasswordDTO, @PathVariable long appUserId) throws NoSuchUserByIdException, PassWordMissMachException, InvalidRegexParameterException, BelongToAnotherUserException {
    log.info("REST POST rest/reset-password started");
    appUserService.changePassword(changePasswordDTO, appUserId);
    log.info("REST POST rest/reset-password finished");
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
