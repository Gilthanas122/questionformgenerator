package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.appuser.SpecificUserDetails;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUserLoginDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.exceptions.email.ConfirmationTokenDoesNotExistException;
import com.bottomupquestionphd.demo.exceptions.email.EmailAlreadyUsedException;
import com.bottomupquestionphd.demo.repositories.AppUserRepository;
import com.bottomupquestionphd.demo.services.emailService.EmailService;
import com.bottomupquestionphd.demo.services.validations.ErrorServiceImpl;
import com.bottomupquestionphd.demo.services.validations.RegexServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {
  private final AppUserRepository appUserRepository;
  private PasswordEncoder passwordEncoder;
  private final EmailService emailService;

  public AppUserServiceImpl(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
    this.appUserRepository = appUserRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
  }

  @Override
  @Transactional
  public void saveUser(AppUser appUser) throws MissingParamsException, UsernameAlreadyTakenException, EmailAlreadyUsedException, InvalidRegexParameterException {
    ErrorServiceImpl.buildMissingFieldErrorMessage(appUser);
    RegexServiceImpl.checkRegex(appUser.getPassword(), "password");
    RegexServiceImpl.checkRegex(appUser.getEmailId(), "email");
    if (appUserRepository.existsByUsername(appUser.getUsername())) {
      throw new UsernameAlreadyTakenException("The username is already taken");
    }else if (appUserRepository.existByEmailId(appUser.getEmailId())){
      throw new EmailAlreadyUsedException("There is a registration with this email already");
    }
    appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
    this.appUserRepository.save(appUser);
    emailService.sendEmail(appUser);
  }



  @Override
  public AppUserLoginDTO validateLogin(AppUserLoginDTO loginDTO) throws AppUserPasswordMissMatchException, NoSuchUserNameException, InvalidLoginException, MissingParamsException, AppUserNotActivatedException {
    ErrorServiceImpl.buildMissingFieldErrorMessage(loginDTO);
    Optional<AppUser> appUser = appUserRepository.findByUsername(loginDTO.getUsername());
    if (appUser == null || !appUser.isPresent()) {
      throw new NoSuchUserNameException("No such username in the database");
    } else if (!passwordEncoder.matches(loginDTO.getPassword(), appUser.get().getPassword())) {
      throw new AppUserPasswordMissMatchException("Username didn't match the password");
    }else if (!appUser.get().isActive()){
      throw new AppUserNotActivatedException("Your account is not activated, please check your email box for the activation email");
    }
    return loginDTO;
  }

  public AppUser findCurrentlyLoggedInUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    SpecificUserDetails specificUserDetails = (SpecificUserDetails) auth.getPrincipal();
    AppUser appUser = appUserRepository.findByUsername(specificUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Couldn't find user with the given username"));
    return appUser;
  }

  @Override
  public AppUser findById(long appUserId) throws NoSuchUserByIdException {
    return appUserRepository.findById(appUserId).orElseThrow(() -> new NoSuchUserByIdException("Couldn't find appuser with the given id"));
  }

  @Override
  public void checkIfCurrentUserMatchesUserIdInPath(long appUserId) throws BelongToAnotherUserException {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    SpecificUserDetails specificUserDetails = (SpecificUserDetails) auth.getPrincipal();
    AppUser appUser = appUserRepository.findByUsername(specificUserDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("Couldn't find user with the given username"));
    if (appUser.getId() != appUserId) {
      throw new BelongToAnotherUserException("Current data belongs to another user");
    }
  }

  @Override
  public String activateUserByEmail(String token) throws ConfirmationTokenDoesNotExistException, NoSuchUserByEmailException {
    String userEmail = emailService.findUserByToken(token);
    AppUser appUser = appUserRepository.findByEmailId(userEmail);
    if (appUser == null){
      throw new NoSuchUserByEmailException("No user with the given email");
    }
    appUser.setActive(true);
    appUserRepository.save(appUser);

    return "You have successfully registered with the email " + userEmail;
  }
}
