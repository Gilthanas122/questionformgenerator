package com.bottomupquestionphd.demo.integrationstests;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.services.emailService.EmailService;
import com.bottomupquestionphd.demo.testconfiguration.SmtpServerRule;
import com.bottomupquestionphd.demo.testconfiguration.TestConfigurationBeanFactory;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@SpringBootTest
@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
@ActiveProfiles("test")
public class PublicRestControllerEmailTest {

  @Autowired
  private EmailService emailService;
  @Autowired
  private BeanFactory beanFactory;


  @Rule
  public SmtpServerRule smtpServerRule = new SmtpServerRule(2525);

  @Test
  public void postRegister_EmailSent() throws MessagingException, IOException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    emailService.sendEmail(appUser);

    MimeMessage[] receivedMessages = smtpServerRule.getMessages();
    assertEquals(1, receivedMessages.length);

    MimeMessage current = receivedMessages[0];

    assertEquals("questionformhello@gmail.com", current.getSubject());
    assertEquals(appUser.getEmailId(), current.getAllRecipients()[0].toString());
    assertTrue(String.valueOf(current.getContent()).contains("To confirm the account please click here http://localhost:8080/verify-account"));
  }
}
