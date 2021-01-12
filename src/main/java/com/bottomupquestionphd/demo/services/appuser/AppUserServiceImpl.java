package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.dtos.appuser.LoginDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.repositories.AppUserRepository;
import com.bottomupquestionphd.demo.services.errors.ErrorService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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


}
