package com.bottomupquestionphd.demo.unittests;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.services.error.ErrorService;
import com.bottomupquestionphd.demo.services.error.ErrorServiceImpl;
import com.bottomupquestionphd.demo.testconfiguration.TestConfigurationBeanFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
public class ErrorServiceTest {
  private ErrorService errorService;


  @Before
  public void setup(){
    errorService = new ErrorServiceImpl();
  }

  @Autowired
  private BeanFactory beanFactory;

  @Test
  public void buildMissingFieldsErrorMessage_withNoNullOrEmptyFields_returnsEmptyString(){
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");

    String error = errorService.buildMissingFieldErrorMessage(appUser);

    Assert.assertEquals(error, null);
  }

  @Test
  public void buildMissingFieldsErrorMessage_withOneMissingField_returnsEmptyFieldname(){
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setPassword(null);

    String error = errorService.buildMissingFieldErrorMessage(appUser);

    Assert.assertEquals(error, "Password is required");
  }

  @Test
  public void buildMissingFieldsErrorMessage_withTwoMissingField_returnsEmptyFieldsnames(){
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    appUser.setPassword(null);
    appUser.setUsername("");

    String error = errorService.buildMissingFieldErrorMessage(appUser);

    Assert.assertEquals(error, "Username, password is required");
  }

  @Test(expected = NullPointerException.class)
  public void buildMissingFieldsErrorMessage_withNullObject_returnsEmptyFieldsnames(){
    AppUser appUser = null;

    String error = errorService.buildMissingFieldErrorMessage(appUser);
  }

}
