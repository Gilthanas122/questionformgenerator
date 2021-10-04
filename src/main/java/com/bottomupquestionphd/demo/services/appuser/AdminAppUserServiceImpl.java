package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.repositories.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminAppUserServiceImpl implements AdminAppUserService{
  private final AppUserRepository appUserRepository;
  private final AppUserService appUserService;

  public AdminAppUserServiceImpl(AppUserRepository appUserRepository, AppUserService appUserService) {
    this.appUserRepository = appUserRepository;
    this.appUserService = appUserService;
  }

  @Override
  public List<AppUser> findAllUsers() throws NoUsersInDatabaseException {
    if (appUserRepository.count() < 1){
      throw new NoUsersInDatabaseException("No users in database");
    }
    long appUserId = appUserService.findCurrentlyLoggedInUser().getId();
    return appUserRepository.findAllCurrentUserNotIncluded(appUserId);
  }

  @Override
  public void addNewRole(String role, long id) throws NoSuchUserByIdException, RoleMissMatchException {
    if (!checkIfValidRoleSuffix(role)){
      throw new RoleMissMatchException("Invalid role");
    }
   AppUser appUser = checkIfUserByIdExists(id);
   String roleConverted = "ROLE_" + role.toUpperCase();
   if (userHasGivenRole(roleConverted, appUser)){
     throw new RoleMissMatchException("User already has the given role");
   }
    appUser.addNewRole(roleConverted);
    appUserRepository.save(appUser);
  }

  private boolean checkIfValidRoleSuffix(String role){
    String roles = "useradminteacher";
    return roles.contains(role);
  }

  private boolean userHasGivenRole(String role, AppUser appUser) {
   return appUser.getRoles().contains(role);
  }

  private AppUser checkIfUserByIdExists(long id) throws NoSuchUserByIdException {
    return appUserRepository.findById(id).orElseThrow(() -> new NoSuchUserByIdException("No user at the given id"));
  }

  @Override
  public void removeRole(String role, long id) throws NoSuchUserByIdException, RoleMissMatchException {
    AppUser appUser = checkIfUserByIdExists(id);
    String roleConverted = "ROLE_" + role.toUpperCase();
    if (!checkIfValidRoleSuffix(role)){
      throw new RoleMissMatchException("Invalid role");
    }else if (!userHasGivenRole(roleConverted, appUser)){
      throw new RoleMissMatchException("User doesn't have the given role");
    }else{
      StringBuilder newRoles = new StringBuilder();
      for (String r: appUser.getRoles().split(",")) {
        if (!r.equals(roleConverted)){
          newRoles.append(r + ",");
        }
      }
      appUser.setRoles(newRoles.substring(0, newRoles.length()-1));
      appUserRepository.save(appUser);
    }
  }

  @Override
  public void deactivateUser(long id) throws NoSuchUserByIdException, UserDeactivateException {
    AppUser appUser = checkIfUserByIdExists(id);
    if (!appUser.isActive()){
      throw new UserDeactivateException("User is already inactive");
    }
    appUser.setActive(false);
    appUserRepository.save(appUser);
  }

  @Override
  public void activateUser(long id) throws NoSuchUserByIdException, UserDeactivateException {
    AppUser appUser = checkIfUserByIdExists(id);
    if (appUser.isActive()){
      throw new UserDeactivateException("User is already active");
    }
    appUser.setActive(true);
    appUserRepository.save(appUser);
  }

  @Override
  public void deleteUser(long id) throws NoSuchUserByIdException, UserAlreadyDisabledException {
    AppUser appUser = checkIfUserByIdExists(id);
    if (appUser.isDisabled()){
      throw new UserAlreadyDisabledException("User is already disabled.");
    }
    appUserRepository.setUserToDisabled(appUser.getId());
  }
}
