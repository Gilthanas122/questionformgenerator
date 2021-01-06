package com.bottomupquestionphd.demo.services;

import com.bottomupquestionphd.demo.repositories.AppUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService {
  private final AppUserRepository appUserRepository;

  public AppUserServiceImpl(AppUserRepository appUserRepository) {
    this.appUserRepository = appUserRepository;
  }
}
