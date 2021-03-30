package com.bottomupquestionphd.demo.controllers.restcontroller;

import com.bottomupquestionphd.demo.controllers.PublicController;
import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.dtos.SuccessMessageDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUserLoginDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUserRegisterDTO;
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
  public ResponseEntity<?> renderAppUserCreateForm(){
    log.info("GET /rest/register started");
    log.info("GET /rest/register finished");
    return new ResponseEntity<>(new AppUser(), HttpStatus.OK);
  }

  @PostMapping("register")
  public ResponseEntity<?> saveUser(@RequestBody AppUserRegisterDTO appUserRegisterDTO) throws InvalidRegexParameterException, EmailAlreadyUsedException, MissingParamsException, UsernameAlreadyTakenException {
    log.info("POST /register started");
    AppUser appUser = new AppUser.Builder().username(appUserRegisterDTO.username).password(appUserRegisterDTO.getPassword()).emailId(appUserRegisterDTO.getEmail()).build();
    appUserService.saveUser(appUser);
    log.info("POST /register finished");
    return new ResponseEntity(new SuccessMessageDTO("created", "App user successfully registered"), HttpStatus.CREATED);
  }

  @GetMapping("login")
  public ResponseEntity<?> createLoginDTO(){
    log.info("GET /rest/login started");
    log.info("GET /rest/login finished");
    return new ResponseEntity<>(new AppUserLoginDTO(), HttpStatus.OK);
  }

  @PostMapping("login")
  public ResponseEntity<?> checkLogin(@RequestBody AppUserLoginDTO appUserLoginDTO) throws AppUserNotActivatedException, NoSuchUserNameException, InvalidLoginException, MissingParamsException, AppUserPasswordMissMatchException {
    log.info("POST /login started");
    appUserService.validateLogin(appUserLoginDTO);
    log.info("POST /login finished");
    return new ResponseEntity<>(new SuccessMessageDTO("ok","Successful login"), HttpStatus.OK);
  }

  @GetMapping("verify-account")
  public ResponseEntity<?> verifyUserAccount(@RequestParam String token) throws ConfirmationTokenDoesNotExistException, NoSuchUserByEmailException {
    log.info("/GET verify-account started");
    String message = appUserService.activateUserByEmail(token);
    log.info("/GET verify-account finished");
    return new ResponseEntity<>(new SuccessMessageDTO("ok", message), HttpStatus.OK);
  }
}
