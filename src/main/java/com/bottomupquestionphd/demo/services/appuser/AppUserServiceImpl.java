package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.dtos.AppUserTokenDTO;
import com.bottomupquestionphd.demo.domains.dtos.LoginDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.*;
import com.bottomupquestionphd.demo.repositories.AppUserRepository;
import com.bottomupquestionphd.demo.services.errors.ErrorService;
import com.bottomupquestionphd.demo.utilities.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService {
  private final AppUserRepository appUserRepository;
  private final ErrorService errorService;
  private PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;


  public AppUserServiceImpl(AppUserRepository appUserRepository, ErrorService errorService, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
    this.appUserRepository = appUserRepository;
    this.errorService = errorService;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
  }

  @Override
  public void saveUser(AppUser appUser) throws MissingParamsException, UsernameAlreadyTakenException, PasswordNotComplexEnoughException {
    String errorMessage = errorService.buildMissingFieldErrorMessage(appUser);
    if (errorMessage != null){
      throw  new MissingParamsException(errorMessage);
    }else if (!checkPasswordComplexity(appUser.getPassword())){
      throw new PasswordNotComplexEnoughException("Password must be at least 8 character long, at least one uppercase letter and one number and special character of the following ones: @#$%^&+-=");
    }
    else if (appUserRepository.existsByUsername(appUser.getUsername())){
      throw new UsernameAlreadyTakenException("The username is already taken");
    }
    appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
    appUser.setActive(1);
    this.appUserRepository.save(appUser);
  }

  private boolean checkPasswordComplexity(String password) {
    String regex = "^(?=.*[0-9])"
      + "(?=.*[a-z])(?=.*[A-Z])"
      + "(?=.*[@#$%^&+-=])"
      + "(?=\\S+$).{8,20}$";
    return password.matches(regex);
  }

  @Override
  public AppUserTokenDTO validateLogin(LoginDTO loginDTO) throws AppUserPasswordMissMatchException, NoSuchUserNameException, InvalidLoginException {
    String errorMessage = errorService.buildMissingFieldErrorMessage(loginDTO);
    if (errorMessage != null){
      throw new InvalidLoginException(errorMessage);
    }
    AppUser appUser = appUserRepository.findByUsername(loginDTO.getUsername());
    if (appUser == null){
      throw new NoSuchUserNameException("No such username in the database");
    }
    else if (!passwordEncoder.matches(loginDTO.getPassword(), appUser.getPassword())) {
      throw new AppUserPasswordMissMatchException("Username didn't match the password");
    }

    return  authentication(loginDTO);
  }

  @Override
  public AppUserTokenDTO authentication(LoginDTO playerLoginRequestDTO)  {
    AppUser appUser = new AppUser(playerLoginRequestDTO.getUsername(), playerLoginRequestDTO.getPassword());
    AppUserPrincipal appUserPrincipal = new AppUserPrincipal(appUser);
    try {
      this.authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(appUserPrincipal, appUser.getPassword(), appUserPrincipal.getAuthorities()));
    } catch (BadCredentialsException e) {
      e.printStackTrace();
      throw new UsernameNotFoundException("Username or password is incorrect.");
    }
    final UserDetails userDetails = new AppUserPrincipalDetailsService(appUserRepository).loadUserByUsername(playerLoginRequestDTO.getUsername());
    AppUserTokenDTO successUser = new AppUserTokenDTO();
    successUser.setStatus("ok");
    successUser.setToken(new JwtUtil().generateToken(userDetails));
    return successUser;
  }

  @Override
  public AppUser findPlayerByToken() {
    return appUserRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
  }

}
