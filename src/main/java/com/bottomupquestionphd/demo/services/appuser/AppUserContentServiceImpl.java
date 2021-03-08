package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUsersQuestionFormsDTO;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.services.answerforms.AnswerFormService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserContentServiceImpl implements AppUserContentService {
    private final AnswerFormService answerFormService;

    public AppUserContentServiceImpl(AnswerFormService answerFormService) {
        this.answerFormService = answerFormService;
    }

    @Override
    public List<AppUsersQuestionFormsDTO> findQuestionFormBelongingToUser(long appUserId) throws NoSuchUserByIdException, BelongToAnotherUserException {
        List<AppUsersQuestionFormsDTO> appUsersQuestionFormsDTOS = answerFormService.findQuestionFormsFilledOutByAppUserId(appUserId);
        return answerFormService.findQuestionFormsFilledOutByAppUserId(appUserId);
    }
}
