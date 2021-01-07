package com.bottomupquestionphd.demo.services.appuser;


import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.appuser.MyUserDetails;
import com.bottomupquestionphd.demo.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Qualifier("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {

  @Autowired
  AppUserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    Optional<AppUser> user = userRepository.findByUsername(userName);

    user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));

    return user.map(MyUserDetails::new).get();
  }
}
