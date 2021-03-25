package com.bottomupquestionphd.demo.unittests;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.services.validations.ErrorServiceImpl;
import com.bottomupquestionphd.demo.testconfiguration.TestConfigurationBeanFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
public class ErrorServiceTest {

  @Autowired
  private BeanFactory beanFactory;

  @Test
  public void buildMissingFieldsErrorMessage_withNoNullOrEmptyFields_returnsEmptyString() throws MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");

    String error = ErrorServiceImpl.buildMissingFieldErrorMessage(appUser);

    Assert.assertEquals("", error);
  }

  @Test(expected = MissingParamsException.class)
  public void buildMissingFieldsErrorMessage_withOneMissingField_throwsMissingParamsException() throws MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setPassword(null);

    ErrorServiceImpl.buildMissingFieldErrorMessage(appUser);
  }

  @Test(expected = MissingParamsException.class)
  public void buildMissingFieldsErrorMessage_withTwoMissingField_throwsMissingParamsException() throws MissingParamsException {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setPassword(null);
    appUser.setUsername("");

    ErrorServiceImpl.buildMissingFieldErrorMessage(appUser);
  }

  @Test(expected = NullPointerException.class)
  public void buildMissingFieldsErrorMessage_withNullObject_throwsNullPointerException() throws MissingParamsException {
    AppUser appUser = null;

    ErrorServiceImpl.buildMissingFieldErrorMessage(appUser);
  }
}
