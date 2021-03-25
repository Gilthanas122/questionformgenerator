package com.bottomupquestionphd.demo.services.emailService;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.tokens.ConfirmationToken;
import com.bottomupquestionphd.demo.exceptions.email.ConfirmationTokenDoesNotExistException;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

  void sendEmail(AppUser appUser);

  ConfirmationToken createTokenForAppUser(AppUser appUser);

  String findUserByToken(String token) throws ConfirmationTokenDoesNotExistException;
}
