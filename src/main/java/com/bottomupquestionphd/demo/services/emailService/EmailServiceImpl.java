package com.bottomupquestionphd.demo.services.emailService;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.tokens.ConfirmationToken;
import com.bottomupquestionphd.demo.exceptions.email.ConfirmationTokenDoesNotExistException;
import com.bottomupquestionphd.demo.repositories.ConfirmationTokenRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class EmailServiceImpl implements EmailService {
  private JavaMailSender javaMailSender;
  private final ConfirmationTokenRepository confirmationTokenRepository;

  public EmailServiceImpl(JavaMailSender javaMailSender, ConfirmationTokenRepository confirmationTokenRepository) {
    this.javaMailSender = javaMailSender;
    this.confirmationTokenRepository = confirmationTokenRepository;
  }

  @Async
  @Override
  public void sendEmail(AppUser appUser) {

    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(appUser.getEmailId());
    message.setSubject("Complete the Registration");
    message.setFrom(System.getenv("EMAIL_USERNAME"));
    message.setText("To confirm the account please click here http://localhost:8080/verify-account" +
            "?token=" + appUser.getConfirmationToken().getConfirmationToken());
    javaMailSender.send(message);

    confirmationTokenRepository.save(appUser.getConfirmationToken());
  }

  @Override
  public ConfirmationToken createTokenForAppUser(AppUser appUser) {
    ConfirmationToken confirmationToken = new ConfirmationToken
            .Builder()
            .appUser(appUser)
            .confirmationToken()
            .createdDate(new Date())
            .build();
    return confirmationToken;
  }

  @Override
  public boolean verifyEmailPattern(String emailId) {
    String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    CharSequence chr = emailId;
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(chr);
    return  matcher.matches();
  }

  @Override
  public String findUserByToken(String token) throws ConfirmationTokenDoesNotExistException {
    ConfirmationToken confirmationToken = confirmationTokenRepository.findByConfirmationToken(token);
    if (confirmationToken == null){
      throw new ConfirmationTokenDoesNotExistException("Give confirmation token does not exist");
    }
    return confirmationToken.getAppUser().getEmailId();
  }

}
