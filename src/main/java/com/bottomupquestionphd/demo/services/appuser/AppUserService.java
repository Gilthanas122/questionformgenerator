package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.dtos.AppUserTokenDTO;
import com.bottomupquestionphd.demo.domains.dtos.LoginDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import org.springframework.stereotype.Service;

@Service
public interface AppUserService {

  void saveUser(AppUser appUser) throws MissingParamsException, UsernameAlreadyTakenException, PasswordNotComplexEnoughException;

  AppUser findPlayerByToken();

  AppUserTokenDTO authentication(LoginDTO playerLoginRequestDTO);

  AppUserTokenDTO validateLogin(LoginDTO loginDTO) throws AppUserPasswordMissMatchException, NoSuchUserNameException, InvalidLoginException;
}
