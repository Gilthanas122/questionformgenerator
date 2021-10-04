package com.bottomupquestionphd.demo.unittests.appuser;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.repositories.AppUserRepository;
import com.bottomupquestionphd.demo.services.appuser.AdminAppUserService;
import com.bottomupquestionphd.demo.services.appuser.AdminAppUserServiceImpl;
import com.bottomupquestionphd.demo.testconfiguration.TestConfigurationBeanFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
public class AdminServiceTest {
  private final AppUserRepository appUserRepository = Mockito.mock(AppUserRepository.class);
  private AdminAppUserService adminAppUserService;

  @Autowired
  private BeanFactory beanFactory;

  @Before
  public void setup() {
    this.adminAppUserService = new AdminAppUserServiceImpl(appUserRepository);
  }

  @Test
  public void findAllUsers_withMoreThanOneUserInDB_returnsListOfAppUsers() throws NoUsersInDatabaseException {
    List<AppUser> appUsers = (List<AppUser>) beanFactory.getBean("validUsers");

    Mockito.when(appUserRepository.count()).thenReturn(4l);
    Mockito.when(appUserRepository.findAll()).thenReturn(appUsers);

    List<AppUser> returnedAppUsers = adminAppUserService.findAllUsers();

    Mockito.verify(appUserRepository, times(1)).findAll();
    Assert.assertEquals("ROLE_USER,ROLE_ADMIN", returnedAppUsers.get(0).getRoles());
    Assert.assertEquals("ROLE_USER", returnedAppUsers.get(1).getRoles());
  }

  @Test(expected = NoUsersInDatabaseException.class)
  public void findAllUsers_withNoUsersInDB_throwsNoUsersInDatabaseException() throws NoUsersInDatabaseException {
    List<AppUser> appUsers = (List<AppUser>) beanFactory.getBean("validUsers");

    Mockito.when(appUserRepository.count()).thenReturn(0l);

    adminAppUserService.findAllUsers();
  }

  @Test
  public void addNewRole_withValidRole() throws NoSuchUserByIdException, RoleMissMatchException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    String role = "teacher";

    Mockito.when(appUserRepository.findById(appUser.getId())).thenReturn(java.util.Optional.of(appUser));

    adminAppUserService.addNewRole(role, appUser.getId());
  }

  @Test(expected = RoleMissMatchException.class)
  public void addNewRole_withInValidRole_throwsRoleMissMatchException() throws NoSuchUserByIdException, RoleMissMatchException {
    String role = "kaki";
    long appUserId = 1;

    adminAppUserService.addNewRole(role, appUserId);
  }

  @Test(expected = RoleMissMatchException.class)
  public void addNewRole_withRoleThatTheUserAlreadyHas_throwsRoleMissMatchException() throws NoSuchUserByIdException, RoleMissMatchException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    String role = "user";

    Mockito.when(appUserRepository.findById(appUser.getId())).thenReturn(java.util.Optional.of(appUser));

    adminAppUserService.addNewRole(role, appUser.getId());
  }

  @Test(expected = NoSuchUserByIdException.class)
  public void addNewRole_withInvalidAppUserId_throwsNoSuchUserByIdException() throws NoSuchUserByIdException, RoleMissMatchException {
    String role = "teacher";
    long appUserId = 1;

    Mockito.when(appUserRepository.findById(appUserId)).thenReturn(java.util.Optional.ofNullable(null));

    adminAppUserService.addNewRole(role, appUserId);
  }

  @Test
  public void removeRole_withValidRoleAndId() throws NoSuchUserByIdException, RoleMissMatchException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.addNewRole("ROLE_TEACHER");
    String role = "teacher";

    Mockito.when(appUserRepository.findById(appUser.getId())).thenReturn(java.util.Optional.of(appUser));

    adminAppUserService.removeRole(role, appUser.getId());

    Mockito.verify(appUserRepository, times(1)).save(appUser);
  }

  @Test(expected = RoleMissMatchException.class)
  public void removeRole_InValidRoleSuffix_throwsRoleMissMatchException() throws NoSuchUserByIdException, RoleMissMatchException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.addNewRole("ROLE_TEACHER");
    String role = "kaki";

    Mockito.when(appUserRepository.findById(appUser.getId())).thenReturn(java.util.Optional.of(appUser));

    adminAppUserService.removeRole(role, appUser.getId());
  }

  @Test(expected = RoleMissMatchException.class)
  public void removeRole_withRoleUserDoesntHave_throwsRoleMissMatchException() throws NoSuchUserByIdException, RoleMissMatchException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    String role = "teacher";

    Mockito.when(appUserRepository.findById(appUser.getId())).thenReturn(java.util.Optional.of(appUser));

    adminAppUserService.removeRole(role, appUser.getId());
  }

  @Test
  public void deactivateUser_withvalidIdAndActiveUser() throws UserDeactivateException, NoSuchUserByIdException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setActive(true);

    Mockito.when(appUserRepository.findById(appUser.getId())).thenReturn(java.util.Optional.of(appUser));

    adminAppUserService.deactivateUser(appUser.getId());
  }

  @Test(expected = UserDeactivateException.class)
  public void deactivateUser_withInactiveUser_throwsUserDeactivateException() throws UserDeactivateException, NoSuchUserByIdException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setActive(false);

    Mockito.when(appUserRepository.findById(appUser.getId())).thenReturn(java.util.Optional.of(appUser));

    adminAppUserService.deactivateUser(appUser.getId());
  }

  @Test
  public void activateUser_withvalidIdAndActiveUser() throws UserDeactivateException, NoSuchUserByIdException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setActive(false);

    Mockito.when(appUserRepository.findById(appUser.getId())).thenReturn(java.util.Optional.of(appUser));

    adminAppUserService.activateUser(appUser.getId());
  }

  @Test(expected = UserDeactivateException.class)
  public void activateUser_alreadydActiveUser_throwsUserDeactivateException() throws UserDeactivateException, NoSuchUserByIdException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setActive(true);
    Mockito.when(appUserRepository.findById(appUser.getId())).thenReturn(java.util.Optional.of(appUser));

    adminAppUserService.activateUser(appUser.getId());
  }

  @Test
  public void deleteUser_withValidUser() throws NoSuchUserByIdException, UserAlreadyDisabledException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");

    Mockito.when(appUserRepository.findById(appUser.getId())).thenReturn(java.util.Optional.of(appUser));

    adminAppUserService.deleteUser(appUser.getId());

    Mockito.verify(appUserRepository, times(1)).setUserToDisabled(appUser.getId());
  }

  @Test(expected = UserAlreadyDisabledException.class)
  public void deleteUser_withAlreadyDisabledUser() throws NoSuchUserByIdException, UserAlreadyDisabledException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setDisabled(true);

    Mockito.when(appUserRepository.findById(appUser.getId())).thenReturn(java.util.Optional.of(appUser));

    adminAppUserService.deleteUser(appUser.getId());
  }
}
