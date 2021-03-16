package com.bottomupquestionphd.demo.services.appuser;

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

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    if (errorMessage != null) {
      throw new MissingParamsException(errorMessage);
    } else if (!checkPasswordComplexity(appUser.getPassword())) {
      throw new PasswordNotComplexEnoughException("Password must be at least 8 and max 20 characters long, at least one uppercase letter and one number and special character of the following ones: @#$%^&+-=");
    } else if (appUserRepository.existsByUsername(appUser.getUsername())) {
      throw new UsernameAlreadyTakenException("The username is already taken");
    }
    appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
    appUser.setActive(true);
    this.appUserRepository.save(appUser);
  }

  private boolean checkPasswordComplexity(String password) {
    String regExpn =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";

    CharSequence chr = password;
    Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(chr);
    return matcher.matches();
  }

  @Override
  public LoginDTO validateLogin(LoginDTO loginDTO) throws AppUserPasswordMissMatchException, NoSuchUserNameException, InvalidLoginException {
    String errorMessage = errorService.buildMissingFieldErrorMessage(loginDTO);
    if (errorMessage != null) {
      throw new InvalidLoginException(errorMessage);
    }
    Optional<AppUser> appUser = appUserRepository.findByUsername(loginDTO.getUsername());
    if (appUser == null || !appUser.isPresent()) {
      throw new NoSuchUserNameException("No such username in the database");
    } else if (!passwordEncoder.matches(loginDTO.getPassword(), appUser.get().getPassword())) {
      throw new AppUserPasswordMissMatchException("Username didn't match the password");
    }

    return loginDTO;
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

  @Override
  public void checkIfCurrentUserMatchesUserIdInPath(long appUserId) throws BelongToAnotherUserException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    MyUserDetails myUserDetails = (MyUserDetails) auth.getPrincipal();
    AppUser appUser = appUserRepository.findByUsername(myUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Couldn't find user with the given username"));
    if (appUser.getId() != appUserId) {
      throw new BelongToAnotherUserException("Current data belongs to another user");
    }
  }
}
