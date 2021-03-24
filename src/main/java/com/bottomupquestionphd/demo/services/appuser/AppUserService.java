package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.dtos.appuser.LoginDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.exceptions.email.ConfirmationTokenDoesNotExistException;
import com.bottomupquestionphd.demo.exceptions.email.EmailAlreadyUserException;
import com.bottomupquestionphd.demo.exceptions.email.InvalidEmailFormatException;
import org.springframework.stereotype.Service;

@Service
public interface AppUserService {

  void saveUser(AppUser appUser) throws MissingParamsException, UsernameAlreadyTakenException, PasswordNotComplexEnoughException, InvalidEmailFormatException, EmailAlreadyUserException;

  LoginDTO validateLogin(LoginDTO loginDTO) throws AppUserPasswordMissMatchException, NoSuchUserNameException, InvalidLoginException, MissingParamsException, AppUserNotActivatedException;

  AppUser findCurrentlyLoggedInUser();

  AppUser findById(long appUserId) throws NoSuchUserByIdException;

  void checkIfCurrentUserMatchesUserIdInPath(long appUserId) throws BelongToAnotherUserException;

  String activateUserByEmail(String token) throws ConfirmationTokenDoesNotExistException, NoSuchUserByEmailException;
}
