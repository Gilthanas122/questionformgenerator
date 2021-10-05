package com.bottomupquestionphd.demo.services.emailService;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.exceptions.email.ConfirmationTokenDoesNotExistException;
import com.bottomupquestionphd.demo.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

  @Autowired
  @Qualifier("gmail")
  private JavaMailSender javaMailSender;
  private final AppUserRepository appUserRepository;

  public EmailServiceImpl(JavaMailSender javaMailSender, AppUserRepository appUserRepository) {
    this.javaMailSender = javaMailSender;
    this.appUserRepository = appUserRepository;
  }

  @Async
  @Override
  public void sendEmail(AppUser appUser) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(appUser.getEmailId());
    message.setSubject("Complete the Registration");
    message.setFrom(System.getenv("EMAIL_USERNAME"));
    message.setText("To confirm the account please click here http://localhost:8080/verify-account" +
            "?token=" + appUser.getConfirmationToken());
    javaMailSender.send(message);
  }

  @Override
  public String findUserByToken(String token) throws ConfirmationTokenDoesNotExistException {
    AppUser appUser = appUserRepository.findByConfirmationToken(token);
    if (appUser == null){
      throw new ConfirmationTokenDoesNotExistException("Given confirmation token does not exist");
    }
    return appUser.getEmailId();
  }

  @Override
  public void sendEmailToChangePassword(AppUser appUser) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(appUser.getEmailId());
    message.setSubject("Change password");
    message.setFrom(System.getenv("EMAIL_USERNAME"));
    message.setText("To change password the account please click here http://localhost:8080/reset-password/"+ appUser.getId() +
            "?token=" + appUser.getConfirmationToken());
    javaMailSender.send(message);
  }

}
