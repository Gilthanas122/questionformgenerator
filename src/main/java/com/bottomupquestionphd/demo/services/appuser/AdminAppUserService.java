package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoUsersInDatabaseException;
import com.bottomupquestionphd.demo.exceptions.appuser.RoleMissMatchException;
import com.bottomupquestionphd.demo.exceptions.appuser.UserDeactivateException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminAppUserService {
  List<AppUser> findAllUsers() throws NoUsersInDatabaseException;

  void addNewRole(String role, long id) throws NoSuchUserByIdException, RoleMissMatchException;

  void removeRole(String role, long id) throws NoSuchUserByIdException, RoleMissMatchException;

  void deactivateUser(long id) throws NoSuchUserByIdException, UserDeactivateException;

  void activateUser(long id) throws NoSuchUserByIdException, UserDeactivateException;

  void deleteUser(long id) throws NoSuchUserByIdException;
}
