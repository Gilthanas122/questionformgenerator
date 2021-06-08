package com.bottomupquestionphd.demo.unittests.appuser;


import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.appuser.SpecificUserDetails;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUserLoginDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.ChangePasswordDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.exceptions.email.EmailAlreadyUsedException;
import com.bottomupquestionphd.demo.exceptions.email.InvalidEmailFormatException;
import com.bottomupquestionphd.demo.repositories.AppUserRepository;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import com.bottomupquestionphd.demo.services.appuser.AppUserServiceImpl;
import com.bottomupquestionphd.demo.services.emailService.EmailService;
import com.bottomupquestionphd.demo.testconfiguration.TestConfigurationBeanFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.bottomupquestionphd.demo.services.validations.ErrorServiceImpl.buildMissingFieldErrorMessage;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
public class AppUserServiceTest {
  private AppUserService appUserService;
  private AppUserRepository appUserRepository;
  private EmailService emailService = Mockito.mock(EmailService.class);

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private BeanFactory beanFactory;

  private Authentication authentication;
  private SecurityContext securityContext;
  private SpecificUserDetails specificUserDetails;

  @Before
  public void setup() {
    authentication = Mockito.mock(Authentication.class);
    securityContext = Mockito.mock(SecurityContext.class);
    appUserRepository = Mockito.mock(AppUserRepository.class);
    appUserService = new AppUserServiceImpl(appUserRepository, passwordEncoder, emailService);
    specificUserDetails = Mockito.mock(SpecificUserDetails.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    Mockito.when(authentication.getPrincipal()).thenReturn(specificUserDetails);
  }

  @Test
  public void saveUser_withValidUser() throws UsernameAlreadyTakenException, MissingParamsException, EmailAlreadyUsedException, InvalidRegexParameterException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");

    appUserService.saveUser(appUser);
    Mockito.verify(appUserRepository, times(1)).save(appUser);
    Mockito.verify(emailService, times(1)).sendEmail(appUser);
  }

  @Test(expected = MissingParamsException.class)
  public void saveUser_withNullUserNameOnAppUser_throwsMissingParamsException() throws UsernameAlreadyTakenException, MissingParamsException, EmailAlreadyUsedException, InvalidRegexParameterException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setUsername(null);

    appUserService.saveUser(appUser);
  }

  @Test(expected = MissingParamsException.class)
  public void saveUser_withNullUserNameOnAppUser_throwMissingParamsException() throws UsernameAlreadyTakenException, MissingParamsException, EmailAlreadyUsedException, InvalidRegexParameterException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setUsername(null);
    Mockito.when(appUserRepository.existsByUsername(appUser.getUsername())).thenReturn(false);

    appUserService.saveUser(appUser);
  }

  @Test(expected = EmailAlreadyUsedException.class)
  public void saveUser_withNullUserNameOnAppUser_throwEmailAlreadyUsedException() throws UsernameAlreadyTakenException, MissingParamsException, EmailAlreadyUsedException, InvalidRegexParameterException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    Mockito.when(appUserRepository.existsByUsername(appUser.getUsername())).thenReturn(false);
    Mockito.when(appUserRepository.existByEmailId(appUser.getEmailId())).thenReturn(true);

    appUserService.saveUser(appUser);
  }

  @Test(expected = MissingParamsException.class)
  public void saveUser_withNullPasswordOnAppUser_throwsMissingParamsException() throws UsernameAlreadyTakenException, MissingParamsException, EmailAlreadyUsedException, InvalidRegexParameterException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setPassword(null);
    when(buildMissingFieldErrorMessage(appUser)).thenThrow(new MissingParamsException());

    appUserService.saveUser(appUser);
  }

  @Test(expected = InvalidRegexParameterException.class)
  public void saveUser_withTooShortPassword_throwsInvalidRegexException() throws UsernameAlreadyTakenException, MissingParamsException, EmailAlreadyUsedException, InvalidRegexParameterException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setPassword("V12-a");

    appUserService.saveUser(appUser);
  }

  @Test(expected = InvalidRegexParameterException.class)
  public void saveUser_withNotExtraCharacterPassword_throwsPasswordNotComplexEnoughException() throws UsernameAlreadyTakenException, MissingParamsException, EmailAlreadyUsedException, InvalidRegexParameterException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setPassword("Hello12334");

    appUserService.saveUser(appUser);
  }

  //Not working with no uppercase letters
/*  @Test(expected = InvalidRegexParameterException.class)
  public void saveUser_withNoUpperCaseLetter_throwsPasswordNotComplexEnoughException() throws UsernameAlreadyTakenException, MissingParamsException, InvalidRegexParameterException, EmailAlreadyUsedException, InvalidEmailFormatException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setPassword("hh++12334");
    appUserService.saveUser(appUser);
  }*/

  @Test(expected = InvalidRegexParameterException.class)
  public void saveUser_withNoNumber_throwsPasswordNotComplexEnoughException() throws UsernameAlreadyTakenException, MissingParamsException, EmailAlreadyUsedException, InvalidRegexParameterException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setPassword("ello++HHH");
    appUserService.saveUser(appUser);
  }

  @Test(expected = UsernameAlreadyTakenException.class)
  public void saveAppUser_withUserNameAlreadyTaken_throwsUserNameAlreadyTakenException() throws UsernameAlreadyTakenException, MissingParamsException, EmailAlreadyUsedException, InvalidRegexParameterException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    when(appUserRepository.existsByUsername(appUser.getUsername())).thenReturn(true);

    appUserService.saveUser(appUser);
  }

  @Test
  public void validateLogin_withValidLoginDTO() throws NoSuchUserNameException, InvalidLoginException, AppUserPasswordMissMatchException, MissingParamsException, AppUserNotActivatedException {
    AppUserLoginDTO appUserLoginDTO = (AppUserLoginDTO) beanFactory.getBean("validLoginDTO");
    Optional<AppUser> appUser = Optional.of((AppUser) beanFactory.getBean("validUser"));
    appUser.get().setPassword(passwordEncoder.encode(appUser.get().getPassword()));
    when(appUserRepository.findByUsername(appUserLoginDTO.getUsername())).thenReturn(appUser);

    appUserService.validateLogin(appUserLoginDTO);
  }

  @Test(expected = MissingParamsException.class)
  public void validateLogin_withNullUserName_throwsInvalidLoginException() throws NoSuchUserNameException, InvalidLoginException, AppUserPasswordMissMatchException, MissingParamsException, AppUserNotActivatedException {
    AppUserLoginDTO loginDTO = (AppUserLoginDTO) beanFactory.getBean("validLoginDTO");
    loginDTO.setUsername(null);

    appUserService.validateLogin(loginDTO);
  }

  @Test(expected = MissingParamsException.class)
  public void validateLogin_withNullPassword_throwsInvalidLoginException() throws NoSuchUserNameException, InvalidLoginException, AppUserPasswordMissMatchException, MissingParamsException, AppUserNotActivatedException {
    AppUserLoginDTO loginDTO = (AppUserLoginDTO) beanFactory.getBean("validLoginDTO");
    loginDTO.setPassword(null);
    when(buildMissingFieldErrorMessage(loginDTO)).thenReturn("Null password");

    appUserService.validateLogin(loginDTO);
  }

  @Test(expected = NoSuchUserNameException.class)
  public void validateLogin_withNoAppUserByGivenUserName_throwsNoSuchUserNameException() throws NoSuchUserNameException, InvalidLoginException, AppUserPasswordMissMatchException, MissingParamsException, AppUserNotActivatedException {
    AppUserLoginDTO loginDTO = (AppUserLoginDTO) beanFactory.getBean("validLoginDTO");
    Optional<AppUser> appUser = Optional.of((AppUser) beanFactory.getBean("validUser"));
    appUser.get().setPassword("Helloka++55");
    appUser.get().setPassword(passwordEncoder.encode(appUser.get().getPassword()));
    when(appUserRepository.findByUsername(loginDTO.getUsername())).thenReturn(null);

    appUserService.validateLogin(loginDTO);
  }

  @Test(expected = AppUserPasswordMissMatchException.class)
  public void validateLogin_withNotMatchingPassword_throwsAppUserPasswordMissmatchException() throws NoSuchUserNameException, InvalidLoginException, AppUserPasswordMissMatchException, MissingParamsException, AppUserNotActivatedException {
    AppUserLoginDTO loginDTO = (AppUserLoginDTO) beanFactory.getBean("validLoginDTO");
    Optional<AppUser> appUser = Optional.of((AppUser) beanFactory.getBean("validUser"));
    appUser.get().setPassword("Helloka++55");
    appUser.get().setPassword(passwordEncoder.encode(appUser.get().getPassword()));
    when(appUserRepository.findByUsername(loginDTO.getUsername())).thenReturn(appUser);

    appUserService.validateLogin(loginDTO);
  }

  @Test
  public void findById_withExistingUser_returnUser() throws NoSuchUserByIdException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    when(appUserRepository.findById(appUser.getId())).thenReturn(Optional.of(appUser));

    AppUser returnAppUser = appUserService.findById(appUser.getId());
    Mockito.verify(appUserRepository, times(1)).findById(appUser.getId());
    Assert.assertEquals(appUser.getUsername(), returnAppUser.getUsername());
    Assert.assertEquals(appUser.getPassword(), returnAppUser.getPassword());
  }

  @Test(expected = NoSuchUserByIdException.class)
  public void findById_withNonExistingUser_throwsNoSuchUserByIdException() throws NoSuchUserByIdException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");

    appUserService.findById(appUser.getId());
  }

  @Test
  public void checkIfCurrentUserMatchesUserIdInPath_withValidUserId() throws BelongToAnotherUserException {
    Authentication authentication = Mockito.mock(Authentication.class);
    SecurityContext securityContext = Mockito.mock(SecurityContext.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    SpecificUserDetails specificUserDetails = Mockito.mock(SpecificUserDetails.class);

    SecurityContextHolder.setContext(securityContext);

    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    Mockito.when(appUserRepository.findByUsername(any())).thenReturn(Optional.of(appUser));
    Mockito.when(authentication.getPrincipal()).thenReturn(specificUserDetails);
    Mockito.when(specificUserDetails.getUsername()).thenReturn(appUser.getUsername());
    appUserService.checkIfCurrentUserMatchesUserIdInPath(appUser.getId());
    Mockito.verify(appUserRepository, times(1)).findByUsername(any());
  }

  @Test(expected = BelongToAnotherUserException.class)
  public void checkIfCurrentUserMatchesUserIdInPath_withInvalidUserId_throwsBelongToAnotherUserException() throws BelongToAnotherUserException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    AppUser appUser1 = (AppUser) beanFactory.getBean("validUser");
    appUser1.setId(2);
    Mockito.when(appUserRepository.findByUsername(any())).thenReturn(Optional.of(appUser1));
    Mockito.when(specificUserDetails.getUsername()).thenReturn(appUser.getUsername());

    appUserService.checkIfCurrentUserMatchesUserIdInPath(0);
  }

  @Test(expected = UsernameNotFoundException.class)
  public void checkIfCurrentUserMatchesUserIdInPath_withInvalidUserName_throwsUserNameNotFound() throws BelongToAnotherUserException {

    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    Mockito.when(authentication.getPrincipal()).thenReturn(specificUserDetails);
    Mockito.when(specificUserDetails.getUsername()).thenReturn(appUser.getUsername());

    appUserService.checkIfCurrentUserMatchesUserIdInPath(0);
  }

  @Test
  public void findCurrentlyLoggedInUser_withValidUser_returnsAppuser() throws BelongToAnotherUserException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    Mockito.when(appUserRepository.findByUsername(any())).thenReturn(Optional.of(appUser));

    AppUser appUser1 = appUserService.findCurrentlyLoggedInUser();
    Assert.assertEquals(appUser.getPassword(), appUser1.getPassword());
    Assert.assertEquals(appUser.getUsername(), appUser1.getUsername());
  }

  @Test(expected = UsernameNotFoundException.class)
  public void findCurrentlyLoggedInUser_withInValidUser_throwsUserNameNotFound() throws BelongToAnotherUserException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    Mockito.when(specificUserDetails.getUsername()).thenReturn(appUser.getUsername());

    appUserService.findCurrentlyLoggedInUser();
  }

  @Test
  public void activateUserByEmail_withValidToken_returnSuccessMesssage() throws NoSuchUserByEmailException, AppUserIsAlreadyActivatedException {
    AppUser appUser = (AppUser) beanFactory.getBean("inactiveUser");
    Mockito.when(appUserRepository.findByConfirmationToken(appUser.getConfirmationToken())).thenReturn(appUser);

    Assert.assertEquals("You have successfully registered with the email " + appUser.getEmailId(), appUserService.activateUserByEmail(appUser.getConfirmationToken()));
    Mockito.verify(appUserRepository, times(1)).save(appUser);
  }

  @Test(expected = AppUserIsAlreadyActivatedException.class)
  public void activateUserByEmail_withAlreadyActivatedUser_throwAppUserIsAlreadyActivatedException() throws NoSuchUserByEmailException, AppUserIsAlreadyActivatedException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    Mockito.when(appUserRepository.findByConfirmationToken(appUser.getConfirmationToken())).thenReturn(appUser);
    appUserService.activateUserByEmail(appUser.getConfirmationToken());
  }

  @Test(expected = NoSuchUserByEmailException.class)
  public void activateUserByEmail_withInvalidToken_throwNoSuchUserByEmailException() throws NoSuchUserByEmailException, AppUserIsAlreadyActivatedException {
    String token = "token";
    Mockito.when(appUserRepository.findByConfirmationToken(token)).thenReturn(null);
    appUserService.activateUserByEmail(token);
  }

  @Test
  public void sendEmailToRegeneratePassword_withValidData() throws AppUserNotActivatedException, InvalidRegexParameterException, NoSuchUserByEmailException, MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");

    Mockito.when(appUserRepository.findByEmailId(any())).thenReturn(appUser);

    appUserService.sendEmailToRegeneratePassword(appUser.getEmailId());
    Mockito.verify(emailService, times(1)).sendEmailToChangePassword(appUser);
  }

  @Test(expected = InvalidRegexParameterException.class)
  public void sendEmailToRegeneratePassword_withInvalidEmail_shouldThrowInvalidRegexparameterException() throws AppUserNotActivatedException, InvalidRegexParameterException, NoSuchUserByEmailException, MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setEmailId("hello.hello.com");

    appUserService.sendEmailToRegeneratePassword(appUser.getEmailId());
  }

  @Test(expected = AppUserNotActivatedException.class)
  public void sendEmailToRegeneratePassword_withInactiveUser_shouldThrowAppUserNotActivatedException() throws AppUserNotActivatedException, InvalidRegexParameterException, NoSuchUserByEmailException, MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setActive(false);

    Mockito.when(appUserRepository.findByEmailId(any())).thenReturn(appUser);

    appUserService.sendEmailToRegeneratePassword(appUser.getEmailId());
  }

  @Test(expected = NoSuchUserByEmailException.class)
  public void sendEmailToRegeneratePassword_withNonExistenEmail_shouldThrowNoSuchUserByEmailException() throws AppUserNotActivatedException, InvalidRegexParameterException, NoSuchUserByEmailException, MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");

    Mockito.when(appUserRepository.findByEmailId(any())).thenReturn(null);

    appUserService.sendEmailToRegeneratePassword(appUser.getEmailId());
  }

  @Test(expected = MissingParamsException.class)
  public void sendEmailToRegeneratePassword_withMissingEmail_shouldThrowMissingParamsException() throws AppUserNotActivatedException, InvalidRegexParameterException, NoSuchUserByEmailException, MissingParamsException {
    appUserService.sendEmailToRegeneratePassword("");
  }

  @Test
  public void changePassword_withValidData() throws NoSuchUserByIdException, PassWordMissMachException, InvalidRegexParameterException {
    ChangePasswordDTO changePasswordDTO = (ChangePasswordDTO) beanFactory.getBean("validChangePasswordDTO");
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");

    Mockito.when(appUserRepository.findById(appUser.getId())).thenReturn(Optional.of(appUser));

    appUserService.changePassword(changePasswordDTO, appUser.getId());

    Mockito.verify(appUserRepository, times(1)).save(any());
  }

  @Test(expected = PassWordMissMachException.class)
  public void changePassword_withNonMatchingPassword_shouldThrowPassWordMissMatchException() throws NoSuchUserByIdException, PassWordMissMachException, InvalidRegexParameterException {
    ChangePasswordDTO changePasswordDTO = (ChangePasswordDTO) beanFactory.getBean("invalidChangePasswordDTO");

    appUserService.changePassword(changePasswordDTO, 1l);
  }

  @Test(expected = InvalidRegexParameterException.class)
  public void changePassword_withNotComplexPassword_shouldThrowsInvalidRegexParameterException() throws NoSuchUserByIdException, PassWordMissMachException, InvalidRegexParameterException {
    ChangePasswordDTO changePasswordDTO = (ChangePasswordDTO) beanFactory.getBean("notComplexChangePasswordDTO");

    appUserService.changePassword(changePasswordDTO, 1l);
  }

  @Test
  public void validateChangePassword_withValidData() throws NoSuchUserByIdException, InvalidChangePasswordException, MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    Mockito.when(appUserRepository.findById(appUser.getId())).thenReturn(Optional.of(appUser));

    appUserService.validateChangePassword(appUser.getId(), appUser.getConfirmationToken());
  }

  @Test(expected = InvalidChangePasswordException.class)
  public void validateChangePassword_withInvalidToken_throwsInvalidChangePasswordException() throws NoSuchUserByIdException, InvalidChangePasswordException, MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    Mockito.when(appUserRepository.findById(appUser.getId())).thenReturn(Optional.of(appUser));

    appUserService.validateChangePassword(appUser.getId(), "validtoken");
  }

  @Test(expected = MissingParamsException.class)
  public void validateChangePassword_withMissingToken_throwsMissingParamsException() throws NoSuchUserByIdException, InvalidChangePasswordException, MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");

    appUserService.validateChangePassword(appUser.getId(), "");
  }
}
