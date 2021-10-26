package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.appuser.AppUserRole;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUserLoginDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.ChangePasswordDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.exceptions.email.EmailAlreadyUsedException;
import com.bottomupquestionphd.demo.repositories.AppUserRepository;
import com.bottomupquestionphd.demo.services.emailService.EmailService;
import com.bottomupquestionphd.demo.services.validations.ErrorServiceImpl;
import com.bottomupquestionphd.demo.services.validations.RegexErrorType;
import com.bottomupquestionphd.demo.services.validations.RegexServiceImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Optional;

@Service
public class AppUserServiceImpl implements AppUserService {
  private final AppUserRepository appUserRepository;
  private final PasswordEncoder passwordEncoder;
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
    RegexServiceImpl.checkRegex(appUser.getPassword(), RegexErrorType.REGEXPASSWORD.toString());
    RegexServiceImpl.checkRegex(appUser.getEmailId(), RegexErrorType.REGEXEMAIL.toString());
    if (appUserRepository.existsByUsername(appUser.getUsername())) {
      throw new UsernameAlreadyTakenException("The username is already taken");
    } else if (appUserRepository.existByEmailId(appUser.getEmailId())) {
      throw new EmailAlreadyUsedException("There is a registration with this email already");
    }
    appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
    this.appUserRepository.save(appUser);
    emailService.sendEmail(appUser);
  }


  @Override
  public AppUserLoginDTO validateLogin(AppUserLoginDTO loginDTO) throws AppUserPasswordMissMatchException, NoSuchUserNameException, MissingParamsException, AppUserNotActivatedException {
    ErrorServiceImpl.buildMissingFieldErrorMessage(loginDTO);
    Optional<AppUser> appUser = appUserRepository.findByUsername(loginDTO.getUsername());
    if (appUser == null || appUser.isEmpty()) {
      throw new NoSuchUserNameException("No such username in the database");
    } else if (!passwordEncoder.matches(loginDTO.getPassword(), appUser.get().getPassword())) {
      throw new AppUserPasswordMissMatchException("Username didn't match the password");
    } else if (!appUser.get().isActive()) {
      throw new AppUserNotActivatedException("Your account is not activated, please check your email box for the activation email");
    }
    return loginDTO;
  }

  public AppUser findCurrentlyLoggedInUser() {
    Principal auth = SecurityContextHolder.getContext().getAuthentication();
    return appUserRepository.findByUsername(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("Couldn't find user with the given username"));
  }

  @Override
  public AppUser findById(long appUserId) throws NoSuchUserByIdException {
    return appUserRepository.findById(appUserId).orElseThrow(() -> new NoSuchUserByIdException("Couldn't find appuser with the given id"));
  }

  @Override
  public void checkIfCurrentUserMatchesUserIdInPath(long appUserId) throws BelongToAnotherUserException {
    AppUser appUser = findCurrentlyLoggedInUser();
    if (appUser.getId() != appUserId && !appUser.getRoles().contains(AppUserRole.ADMIN.toString())) {
      throw new BelongToAnotherUserException("Current data belongs to another user");
    }
  }

  @Override
  public String activateUserByEmail(String token) throws NoSuchUserByEmailException, AppUserIsAlreadyActivatedException, MissingParamsException {
    if (token == null || token.isEmpty()) {
      throw new MissingParamsException("Token is required");
    }
    AppUser appUser = appUserRepository.findByConfirmationToken(token);
    if (appUser == null) {
      throw new NoSuchUserByEmailException("No user with the given email");
    } else if (appUser.isActive()) {
      throw new AppUserIsAlreadyActivatedException("Appuser has been already activated");
    }
    appUser.setActive(true);
    appUserRepository.save(appUser);

    return "You have successfully registered with the email " + appUser.getEmailId();
  }

  @Override
  public void sendEmailToRegeneratePassword(String email) throws AppUserNotActivatedException, NoSuchUserByEmailException, InvalidRegexParameterException {
    RegexServiceImpl.checkRegex(email, RegexErrorType.REGEXEMAIL.toString());
    AppUser appUser = appUserRepository.findByEmailId(email);
    if (appUser == null) {
      throw new NoSuchUserByEmailException("No such user by email. Provide a valid email address");
    } else if (!appUser.isActive()) {
      throw new AppUserNotActivatedException("You should activate your app user in order to login or to reacquire password! Check your email");
    }
    appUser.setConfirmationToken(appUser.generateRandomTokenNotSstatic());
    emailService.sendEmailToChangePassword(appUser);
  }

  @Override
  public void changePassword(ChangePasswordDTO changePasswordDTO, long appUserId) throws NoSuchUserByIdException, PassWordMissMachException, InvalidRegexParameterException, BelongToAnotherUserException {
    if (!changePasswordDTO.getPassword1().equals(changePasswordDTO.getPassword2())) {
      throw new PassWordMissMachException("The two passwords should match");
    }
    RegexServiceImpl.checkRegex(changePasswordDTO.getPassword1(), RegexErrorType.REGEXPASSWORD.toString());
    AppUser appUser = findById(appUserId);
    appUser.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword1()));
    appUserRepository.save(appUser);
  }

  @Override
  public void validateChangePassword(long appUserId, String token) throws NoSuchUserByIdException, InvalidChangePasswordException, MissingParamsException {
    if (token == null || token.isEmpty()) {
      throw new MissingParamsException("Token is required.");
    }
    AppUser appUser = findById(appUserId);
    if (!appUser.getConfirmationToken().equals(token)) {
      throw new InvalidChangePasswordException("The provided confirmation token doesn't belong to the user.");
    }
  }
}
