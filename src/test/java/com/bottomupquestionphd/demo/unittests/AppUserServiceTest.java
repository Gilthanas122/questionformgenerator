package com.bottomupquestionphd.demo.unittests;


import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.appuser.MyUserDetails;
import com.bottomupquestionphd.demo.domains.dtos.appuser.LoginDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.repositories.AppUserRepository;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import com.bottomupquestionphd.demo.services.appuser.AppUserServiceImpl;
import com.bottomupquestionphd.demo.services.error.ErrorService;
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

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
public class AppUserServiceTest {
  private AppUserService appUserService;
  private AppUserRepository appUserRepository;
  private ErrorService errorService;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private BeanFactory beanFactory;

  private Authentication authentication;
  private SecurityContext securityContext;
  private MyUserDetails myUserDetails;

  @Before
  public void setup() {
    authentication = Mockito.mock(Authentication.class);
    securityContext = Mockito.mock(SecurityContext.class);
    appUserRepository = Mockito.mock(AppUserRepository.class);
    errorService = Mockito.mock(ErrorService.class);
    appUserService = new AppUserServiceImpl(appUserRepository, errorService, passwordEncoder);
    myUserDetails = Mockito.mock(MyUserDetails.class);
    Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    Mockito.when(authentication.getPrincipal()).thenReturn(myUserDetails);
  }

  @Test()
  public void saveUser_withValidUser() throws PasswordNotComplexEnoughException, UsernameAlreadyTakenException, MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");

    appUserService.saveUser(appUser);
    Mockito.verify(appUserRepository, times(1)).save(appUser);
  }

  @Test(expected = MissingParamsException.class)
  public void saveUser_withNullUserNameOnAppUser_throwsMissingParamsException() throws PasswordNotComplexEnoughException, UsernameAlreadyTakenException, MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setUsername(null);
    when(errorService.buildMissingFieldErrorMessage(appUser)).thenReturn("Null Username");

    appUserService.saveUser(appUser);
  }

  @Test(expected = MissingParamsException.class)
  public void saveUser_withNullPasswordOnAppUser_throwsMissingParamsException() throws PasswordNotComplexEnoughException, UsernameAlreadyTakenException, MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setPassword(null);
    when(errorService.buildMissingFieldErrorMessage(appUser)).thenReturn("Null Password");

    appUserService.saveUser(appUser);
  }

  @Test(expected = PasswordNotComplexEnoughException.class)
  public void saveUser_withTooShortPassword_throwsPasswordNotComplexEnoughException() throws PasswordNotComplexEnoughException, UsernameAlreadyTakenException, MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setPassword("V12-a");

    appUserService.saveUser(appUser);
  }

  @Test(expected = PasswordNotComplexEnoughException.class)
  public void saveUser_withNotExtraCharacterPassword_throwsPasswordNotComplexEnoughException() throws PasswordNotComplexEnoughException, UsernameAlreadyTakenException, MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setPassword("Hello12334");

    appUserService.saveUser(appUser);
  }

/* NOT CHECKING FOR UPPERCASE
 @Test(expected = PasswordNotComplexEnoughException.class)
    public void saveUser_withNoUpperCaseLetter_throwsPasswordNotComplexEnoughException() throws PasswordNotComplexEnoughException, UsernameAlreadyTakenException, MissingParamsException {
        AppUser appUser = (AppUser) beanFactory.getBean("validUser");
        appUser.setPassword("hh++12334");

        appUserService.saveUser(appUser);
    }*/

  @Test(expected = PasswordNotComplexEnoughException.class)
  public void saveUser_withNoNumber_throwsPasswordNotComplexEnoughException() throws PasswordNotComplexEnoughException, UsernameAlreadyTakenException, MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setPassword("ello++HHH");
    appUserService.saveUser(appUser);
  }

  @Test(expected = UsernameAlreadyTakenException.class)
  public void saveAppUser_withUserNameAlreadyTaken_throwsUserNameAlreadyTakenException() throws PasswordNotComplexEnoughException, UsernameAlreadyTakenException, MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    when(appUserRepository.existsByUsername(appUser.getUsername())).thenReturn(true);

    appUserService.saveUser(appUser);
  }

  @Test
  public void validateLogin_withValidLoginDTO() throws NoSuchUserNameException, InvalidLoginException, AppUserPasswordMissMatchException {
    LoginDTO loginDTO = (LoginDTO) beanFactory.getBean("validLoginDTO");
    Optional<AppUser> appUser = Optional.of((AppUser) beanFactory.getBean("validUser"));
    appUser.get().setPassword(passwordEncoder.encode(appUser.get().getPassword()));
    when(appUserRepository.findByUsername(loginDTO.getUsername())).thenReturn(appUser);

    appUserService.validateLogin(loginDTO);
  }

  @Test(expected = InvalidLoginException.class)
  public void validateLogin_withNullUserName_throwsInvalidLoginException() throws NoSuchUserNameException, InvalidLoginException, AppUserPasswordMissMatchException {
    LoginDTO loginDTO = (LoginDTO) beanFactory.getBean("validLoginDTO");
    loginDTO.setUsername(null);
    when(errorService.buildMissingFieldErrorMessage(loginDTO)).thenReturn("Null UserName");

    appUserService.validateLogin(loginDTO);
  }

  @Test(expected = InvalidLoginException.class)
  public void validateLogin_withNullPassword_throwsInvalidLoginException() throws NoSuchUserNameException, InvalidLoginException, AppUserPasswordMissMatchException {
    LoginDTO loginDTO = (LoginDTO) beanFactory.getBean("validLoginDTO");
    loginDTO.setPassword(null);
    when(errorService.buildMissingFieldErrorMessage(loginDTO)).thenReturn("Null password");

    appUserService.validateLogin(loginDTO);
  }

  @Test(expected = NoSuchUserNameException.class)
  public void validateLogin_withNoAppUserByGivenUserName_throwsNoSuchUserNameException() throws NoSuchUserNameException, InvalidLoginException, AppUserPasswordMissMatchException {
    LoginDTO loginDTO = (LoginDTO) beanFactory.getBean("validLoginDTO");
    Optional<AppUser> appUser = Optional.of((AppUser) beanFactory.getBean("validUser"));
    appUser.get().setPassword("Helloka++55");
    appUser.get().setPassword(passwordEncoder.encode(appUser.get().getPassword()));
    when(appUserRepository.findByUsername(loginDTO.getUsername())).thenReturn(null);

    appUserService.validateLogin(loginDTO);
  }

  @Test(expected = AppUserPasswordMissMatchException.class)
  public void validateLogin_withNotMatchingPassword_throwsAppUserPasswordMissmatchException() throws NoSuchUserNameException, InvalidLoginException, AppUserPasswordMissMatchException {
    LoginDTO loginDTO = (LoginDTO) beanFactory.getBean("validLoginDTO");
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
    MyUserDetails myUserDetails = Mockito.mock(MyUserDetails.class);

    SecurityContextHolder.setContext(securityContext);

    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    Mockito.when(appUserRepository.findByUsername(appUser.getUsername())).thenReturn(Optional.of(appUser));
    Mockito.when(authentication.getPrincipal()).thenReturn(myUserDetails);
    Mockito.when(myUserDetails.getUsername()).thenReturn(appUser.getUsername());
    appUserService.checkIfCurrentUserMatchesUserIdInPath(appUser.getId());
    Mockito.verify(appUserRepository, times(1)).findByUsername(appUser.getUsername());
  }

  @Test(expected = BelongToAnotherUserException.class)
  public void checkIfCurrentUserMatchesUserIdInPath_withInvalidUserId_throwsBelongToAnotherUserException() throws BelongToAnotherUserException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    AppUser appUser1 = (AppUser) beanFactory.getBean("validUser");
    appUser1.setId(2);
    Mockito.when(appUserRepository.findByUsername(appUser.getUsername())).thenReturn(Optional.of(appUser1));
    Mockito.when(myUserDetails.getUsername()).thenReturn(appUser.getUsername());

    appUserService.checkIfCurrentUserMatchesUserIdInPath(0);
  }

  @Test(expected = UsernameNotFoundException.class)
  public void checkIfCurrentUserMatchesUserIdInPath_withInvalidUserName_throwsUserNameNotFound() throws BelongToAnotherUserException {

    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    Mockito.when(authentication.getPrincipal()).thenReturn(myUserDetails);
    Mockito.when(myUserDetails.getUsername()).thenReturn(appUser.getUsername());

    appUserService.checkIfCurrentUserMatchesUserIdInPath(0);
  }

  @Test
  public void findCurrentlyLoggedInUser_withValidUser_returnsAppuser() throws BelongToAnotherUserException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    Mockito.when(myUserDetails.getUsername()).thenReturn(appUser.getUsername());
    Mockito.when(appUserRepository.findByUsername(appUser.getUsername())).thenReturn(Optional.of(appUser));

    AppUser appUser1 = appUserService.findCurrentlyLoggedInUser();
    Assert.assertEquals(appUser.getPassword(), appUser1.getPassword());
    Assert.assertEquals(appUser.getUsername(), appUser1.getUsername());
  }

  @Test(expected = UsernameNotFoundException.class)
  public void findCurrentlyLoggedInUser_withInValidUser_throwsUserNameNotFound() throws BelongToAnotherUserException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    Mockito.when(myUserDetails.getUsername()).thenReturn(appUser.getUsername());

    appUserService.findCurrentlyLoggedInUser();
  }
}
