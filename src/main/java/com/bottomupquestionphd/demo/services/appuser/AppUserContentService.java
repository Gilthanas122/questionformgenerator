package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUsersQuestionFormsDTO;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AppUserContentService {
    List<AppUsersQuestionFormsDTO> findQuestionFormBelongingToUser(long appUserId) throws NoSuchUserByIdException, BelongToAnotherUserException;
}
