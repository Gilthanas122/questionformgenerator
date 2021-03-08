package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import org.springframework.stereotype.Service;

@Service
public class AppUserQuestionFormServiceImpl implements AppUserQuestionFormService {
    private final QuestionFormService questionFormService;

    public AppUserQuestionFormServiceImpl(QuestionFormService questionFormService) {
        this.questionFormService = questionFormService;
    }


    @Override
    public Object findQuestionFormBelongingToUser(long appUserId) {
        return null;
    }
}
