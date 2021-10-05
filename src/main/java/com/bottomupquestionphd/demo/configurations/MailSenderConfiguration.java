package com.bottomupquestionphd.demo.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailSenderConfiguration {

  @Bean("gmail")
  public JavaMailSender gmailMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(System.getenv("EMAIL_HOST"));
    mailSender.setPort(Integer.parseInt(System.getenv("EMAIL_PORT")));

    mailSender.setUsername(System.getenv("EMAIL_USERNAME"));
    mailSender.setPassword(System.getenv("EMAIL_PASSWORD"));

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "false");

    return mailSender;
  }
}
