package com.bottomupquestionphd.demo.unittests.appuser;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUsersQuestionFormsDTO;
import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormNotFilledOutByUserDTO;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.services.answers.answerforms.AnswerFormService;
import com.bottomupquestionphd.demo.services.appuser.AppUserContentService;
import com.bottomupquestionphd.demo.services.appuser.AppUserContentServiceImpl;
import com.bottomupquestionphd.demo.services.appuser.AppUserService;
import com.bottomupquestionphd.demo.services.namedparameterservice.QueryService;
import com.bottomupquestionphd.demo.testconfiguration.TestConfigurationBeanFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@Import(TestConfigurationBeanFactory.class)
public class AppUserContentServiceTest {

  private final AnswerFormService answerFormService = Mockito.mock(AnswerFormService.class);
  private final AppUserService appUserService = Mockito.mock(AppUserService.class);
  private final QueryService queryService = Mockito.mock(QueryService.class);

  @Autowired
  private BeanFactory beanFactory;

  private AppUserContentService appUserContentService;

  @Before
  public void setup() {
    appUserContentService = new AppUserContentServiceImpl(answerFormService, appUserService, queryService);
  }

  @Test
  public void findQuestionFormsFilledOutByUser_withValidUserId() throws BelongToAnotherUserException, NoSuchUserByIdException {
    List<AppUsersQuestionFormsDTO> appUsersQuestionFormsDTOs = (List<AppUsersQuestionFormsDTO>) beanFactory.getBean("appUsersQuestionFormsDTOs");
    long appUserId = 1;
    Mockito.when(answerFormService.findQuestionFormsFilledOutByAppUserId(appUserId)).thenReturn(appUsersQuestionFormsDTOs);

    List<AppUsersQuestionFormsDTO> returnedAppUsersQuestionFormsDTOS = appUserContentService.findQuestionFormsFilledOutByUser(appUserId);

    Assert.assertEquals(appUsersQuestionFormsDTOs.size(), returnedAppUsersQuestionFormsDTOS.size());
    Assert.assertEquals(appUsersQuestionFormsDTOs.get(0).getName(), returnedAppUsersQuestionFormsDTOS.get(0).getName());
    Mockito.verify(answerFormService, times(1)).findQuestionFormsFilledOutByAppUserId(appUserId);
  }

  @Test
  public void findAllQuestionFormsNotFilledOutByUser_withSomeQuestionFormsFilledOut_returnsListQuestionFormNotFilledOutByUserDTOs() throws BelongToAnotherUserException {
    List<QuestionFormNotFilledOutByUserDTO> questionFormNotFilledOutByUserDTOs = (List<QuestionFormNotFilledOutByUserDTO>) beanFactory.getBean("questionFormNotFilledOutByUserDTOs");
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    List<Long> ids = new ArrayList<Long>(List.of(1l, 2l, 3l, 4l));

    Mockito.when(appUserService.findCurrentlyLoggedInUser()).thenReturn(appUser);
    Mockito.when(answerFormService.findQuestionFormIdsFilledOutByUser(appUser.getId())).thenReturn(ids);
    Mockito.when(queryService.findAllQuestionFormNotFilledOutByUser(ids)).thenReturn(questionFormNotFilledOutByUserDTOs);

    List<QuestionFormNotFilledOutByUserDTO> returnedQuestionFormsNotFilledOutByUserDTOs = appUserContentService.findAllQuestionFormsNotFilledOutByUser();

    Assert.assertEquals(questionFormNotFilledOutByUserDTOs.size(), returnedQuestionFormsNotFilledOutByUserDTOs.size());
    Assert.assertEquals(questionFormNotFilledOutByUserDTOs.get(0).getName(), returnedQuestionFormsNotFilledOutByUserDTOs.get(0).getName());
  }

  @Test
  public void findCurrentlyLoggedInUsersId_returnsId() {
    AppUser appUser = (AppUser) beanFactory.getBean("validUser");
    Mockito.when(appUserService.findCurrentlyLoggedInUser()).thenReturn(appUser);

    long id = appUserContentService.findCurrentlyLoggedInUsersId();

    Assert.assertEquals(appUser.getId(), id);
  }

}
