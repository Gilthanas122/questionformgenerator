package com.bottomupquestionphd.demo.services.emailService;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.exceptions.email.ConfirmationTokenDoesNotExistException;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {

  void sendEmail(AppUser appUser);

  String findUserByToken(String token) throws ConfirmationTokenDoesNotExistException;

  void sendEmailToChangePassword(AppUser appUser);
}
