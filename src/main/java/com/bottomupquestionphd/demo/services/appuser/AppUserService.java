package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUserLoginDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.ChangePasswordDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.exceptions.email.EmailAlreadyUsedException;
import org.springframework.stereotype.Service;

@Service
public interface AppUserService {

  void saveUser(AppUser appUser) throws MissingParamsException, UsernameAlreadyTakenException, EmailAlreadyUsedException, InvalidRegexParameterException;

  AppUserLoginDTO validateLogin(AppUserLoginDTO loginDTO) throws AppUserPasswordMissMatchException, NoSuchUserNameException, MissingParamsException, AppUserNotActivatedException;

  AppUser findCurrentlyLoggedInUser();

  AppUser findById(long appUserId) throws NoSuchUserByIdException;

  void checkIfCurrentUserMatchesUserIdInPath(long appUserId) throws BelongToAnotherUserException;

  String activateUserByEmail(String token) throws  NoSuchUserByEmailException, AppUserIsAlreadyActivatedException;

  void sendEmailToRegeneratePassword(String email) throws MissingParamsException, AppUserNotActivatedException, NoSuchUserByEmailException, InvalidRegexParameterException;

  void changePassword(ChangePasswordDTO changePasswordDTO, long appUserId) throws NoSuchUserByIdException, PassWordMissMachException, InvalidRegexParameterException;

  void validateChangePassword(long appUserId, String token) throws NoSuchUserByIdException, InvalidChangePasswordException;
}
