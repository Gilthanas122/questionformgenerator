package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.dtos.appuser.LoginDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import org.springframework.stereotype.Service;

@Service
public interface AppUserService {

  void saveUser(AppUser appUser) throws MissingParamsException, UsernameAlreadyTakenException, PasswordNotComplexEnoughException;

  LoginDTO validateLogin(LoginDTO loginDTO) throws AppUserPasswordMissMatchException, NoSuchUserNameException, InvalidLoginException, MissingParamsException;

  AppUser findCurrentlyLoggedInUser();

  AppUser findById(long appUserId) throws NoSuchUserByIdException;

  void checkIfCurrentUserMatchesUserIdInPath(long appUserId) throws BelongToAnotherUserException;
}
