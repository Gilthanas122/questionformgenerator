package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.appuser.MyUserDetails;
import com.bottomupquestionphd.demo.domains.dtos.appuser.LoginDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.exceptions.email.ConfirmationTokenDoesNotExistException;
import com.bottomupquestionphd.demo.exceptions.email.EmailAlreadyUserException;
import com.bottomupquestionphd.demo.exceptions.email.InvalidEmailFormatException;
import com.bottomupquestionphd.demo.repositories.AppUserRepository;
import com.bottomupquestionphd.demo.services.emailService.EmailService;
import com.bottomupquestionphd.demo.services.error.ErrorServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
  public void saveUser(AppUser appUser) throws MissingParamsException, UsernameAlreadyTakenException, PasswordNotComplexEnoughException, InvalidEmailFormatException, EmailAlreadyUserException {
    ErrorServiceImpl.buildMissingFieldErrorMessage(appUser);
    if (!checkPasswordComplexity(appUser.getPassword())) {
      throw new PasswordNotComplexEnoughException("Password must be at least 8 and max 20 characters long, at least one uppercase letter and one number and special character of the following ones: @#$%^&+-=");
    } else if (appUserRepository.existsByUsername(appUser.getUsername())) {
      throw new UsernameAlreadyTakenException("The username is already taken");
    }else if (!emailService.verifyEmailPattern(appUser.getEmailId())){
      throw new InvalidEmailFormatException("Your email format is not valid!");
    }else if (appUserRepository.existByEmailId(appUser.getEmailId())){
      throw new EmailAlreadyUserException("There is a registration with this email already");
    }
    appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
    appUser.setConfirmationToken(emailService.createTokenForAppUser(appUser));
    this.appUserRepository.save(appUser);
    emailService.sendEmail(appUser);
  }

  private boolean checkPasswordComplexity(String password) {
    String regExpn =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=-])(?=\\S+$).{8,}$";

    CharSequence chr = password;
    Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(chr);
    return matcher.matches();
  }

  @Override
  public LoginDTO validateLogin(LoginDTO loginDTO) throws AppUserPasswordMissMatchException, NoSuchUserNameException, InvalidLoginException, MissingParamsException, AppUserNotActivatedException {
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
