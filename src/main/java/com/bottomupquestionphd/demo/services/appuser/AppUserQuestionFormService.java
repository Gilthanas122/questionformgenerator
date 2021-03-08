package com.bottomupquestionphd.demo.services.appuser;

import org.springframework.stereotype.Service;

@Service
public interface AppUserQuestionFormService {
    Object findQuestionFormBelongingToUser(long appUserId);
}
