package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUsersQuestionFormsDTO;
import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormNotFilledOutByUserDTO;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.services.answerforms.AnswerFormService;
import com.bottomupquestionphd.demo.services.namedparameterservice.QueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserContentServiceImpl implements AppUserContentService {
  private final AnswerFormService answerFormService;
  private final AppUserService appUserService;
  private final QueryService queryService;

  public AppUserContentServiceImpl(AnswerFormService answerFormService, AppUserService appUserService, QueryService queryService) {
    this.answerFormService = answerFormService;
    this.appUserService = appUserService;
    this.queryService = queryService;
  }

  @Override
  public List<AppUsersQuestionFormsDTO> findQuestionFormsFilledOutByUser(long appUserId) throws BelongToAnotherUserException {
    return answerFormService.findQuestionFormsFilledOutByAppUserId(appUserId);
  }

  @Override
  public List<QuestionFormNotFilledOutByUserDTO> findAllQuestionFormsNotFilledOutByUser() throws BelongToAnotherUserException {
    long appUserId = findCurrentlyLoggedInUsersId();
    appUserService.checkIfCurrentUserMatchesUserIdInPath(appUserId);
    List<Long> ids = answerFormService.findQuestionFormIdsFilledOutByUser(appUserId);
    return queryService.findAllQuestionFormNotFilledOutByUser(ids);
  }

  @Override
  public long findCurrentlyLoggedInUsersId() {
    return appUserService.findCurrentlyLoggedInUser().getId();
  }
}
