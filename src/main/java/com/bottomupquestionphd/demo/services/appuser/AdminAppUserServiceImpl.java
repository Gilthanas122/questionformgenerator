package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoUsersInDatabaseException;
import com.bottomupquestionphd.demo.exceptions.appuser.RoleMissMatchException;
import com.bottomupquestionphd.demo.exceptions.appuser.UserDeactivateException;
import com.bottomupquestionphd.demo.repositories.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminAppUserServiceImpl implements AdminAppUserService{
  private final AppUserRepository appUserRepository;

  public AdminAppUserServiceImpl(AppUserRepository appUserRepository) {
    this.appUserRepository = appUserRepository;
  }

  @Override
  public List<AppUser> findAllUsers() throws NoUsersInDatabaseException {
    if (appUserRepository.count() < 1){
      throw new NoUsersInDatabaseException("No users in database");
    }
    return appUserRepository.findAll();
  }

  @Override
  public void addNewRole(String role, long id) throws NoSuchUserByIdException, RoleMissMatchException {
    if (!checkIfValidRoleSuffix(role)){
      throw new RoleMissMatchException("Invalid role");
    }
   AppUser appUser = checkIfUserByIdExists(id);
   String roleConverted = "ROLE_" + role.toUpperCase();
   if (!userHasGivenRole(roleConverted, appUser)){
     appUser.addNewRole(roleConverted);
     appUserRepository.save(appUser);
   }else{
     throw new RoleMissMatchException("User already has the given role");
   }
  }

  private boolean checkIfValidRoleSuffix(String role){
    String roles = "useradminteacher";
    return roles.contains(role);
  }

  private boolean userHasGivenRole(String role, AppUser appUser) {
   return appUser.getRoles().contains(role);
  }

  private AppUser checkIfUserByIdExists(long id) throws NoSuchUserByIdException {
    Optional<AppUser> appUser = appUserRepository.findById(id);
    if (appUser.isEmpty()){
      throw new NoSuchUserByIdException("No user at the given id");
    }
    return appUser.get();
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
          newRoles.append(r);
        }
      }
      appUser.setRoles(newRoles.toString()+ ",");
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
  public void deleteUser(long id) throws NoSuchUserByIdException {
    AppUser appUser = checkIfUserByIdExists(id);
    appUserRepository.setUserToDisabled(appUser.getId());
  }
}
