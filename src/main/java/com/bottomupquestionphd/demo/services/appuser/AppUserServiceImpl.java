package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.appuser.MyUserDetails;
import com.bottomupquestionphd.demo.domains.dtos.appuser.LoginDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.repositories.AppUserRepository;
import com.bottomupquestionphd.demo.services.error.ErrorService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {
  private final AppUserRepository appUserRepository;
  private final ErrorService errorService;
  private PasswordEncoder passwordEncoder;

  public AppUserServiceImpl(AppUserRepository appUserRepository, ErrorService errorService, PasswordEncoder passwordEncoder) {
    this.appUserRepository = appUserRepository;
    this.errorService = errorService;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void saveUser(AppUser appUser) throws MissingParamsException, UsernameAlreadyTakenException, PasswordNotComplexEnoughException {
    String errorMessage = errorService.buildMissingFieldErrorMessage(appUser);
    if (errorMessage != null){
      throw  new MissingParamsException(errorMessage);
    }else if (!checkPasswordComplexity(appUser.getPassword())){
      throw new PasswordNotComplexEnoughException("Password must be at least 8 character long, at least one uppercase letter and one number and special character of the following ones: @#$%^&+-=");
    }
    else if (appUserRepository.existsByUsername(appUser.getUsername())){
      throw new UsernameAlreadyTakenException("The username is already taken");
    }
    appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
    appUser.setActive(true);
    this.appUserRepository.save(appUser);
  }

  private boolean checkPasswordComplexity(String password) {
    String regex = "^(?=.*[0-9])"
      + "(?=.*[a-z])(?=.*[A-Z])"
      + "(?=.*[@#$%^&+-=])"
      + "(?=\\S+$).{8,20}$";
    return password.matches(regex);
  }

  @Override
  public LoginDTO validateLogin(LoginDTO loginDTO) throws AppUserPasswordMissMatchException, NoSuchUserNameException, InvalidLoginException {
    String errorMessage = errorService.buildMissingFieldErrorMessage(loginDTO);
    if (errorMessage != null){
      throw new InvalidLoginException(errorMessage);
    }
    Optional <AppUser> appUser = appUserRepository.findByUsername(loginDTO.getUsername());
    if (!appUser.isPresent()){
      throw new NoSuchUserNameException("No such username in the database");
    }
    else if (!passwordEncoder.matches(loginDTO.getPassword(), appUser.get().getPassword())) {
      throw new AppUserPasswordMissMatchException("Username didn't match the password");
    }

    return  loginDTO;
  }

  @Override
  public AppUser findByUsername(String username) throws NoSuchUserNameException {
    return appUserRepository.findByUsername(username).orElseThrow(() -> new NoSuchUserNameException("Could find user with the given username"));
  }

  public AppUser findCurrentlyLoggedInUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
    AppUser appUser = appUserRepository.findByUsername(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Couldn't find user with the given username"));
    return appUser;
  }

  @Override
  public AppUser findById(long appUserId) throws NoSuchUserByIdException {
    return appUserRepository.findById(appUserId).orElseThrow(() -> new NoSuchUserByIdException("Couldn't find appuser with the given id"));
  }

  public void deleteAnswerFormIfUserHasOneAlready(long answerFormId, AppUser appUser){
    List<AnswerForm> answerForms = appUser.getAnswerForms();
    for (int i = 0; i <answerForms.size() ; i++) {
      if (answerForms.get(i).getId() == answerFormId){
        answerForms.remove(i);
      }
    }
    appUser.setAnswerForms(answerForms);
    appUserRepository.save(appUser);
  }

}
