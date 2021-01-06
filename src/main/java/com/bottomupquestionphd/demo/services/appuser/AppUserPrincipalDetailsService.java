package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import org.springframework.stereotype.Service;
import com.bottomupquestionphd.demo.repositories.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class AppUserPrincipalDetailsService implements UserDetailsService {

  private final AppUserRepository appUserRepository;

  public AppUserPrincipalDetailsService(AppUserRepository appUserRepository) {
    this.appUserRepository = appUserRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    AppUser appUser = appUserRepository.findByUsername(username);
    return new AppUserPrincipal(appUser);
  }
}
