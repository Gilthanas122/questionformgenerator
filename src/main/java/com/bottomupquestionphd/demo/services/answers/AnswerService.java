package com.bottomupquestionphd.demo.services.answers;

import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUsersQuestionFormsDTO;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnswerService {
    void connectQuestionsToAnswers(List<Answer> answers, long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException;

    void connectAnswersToActualAnswers(List<Answer> answers);

    void setActualAnswersToDeleted(long appUserId, long questionFormId);

}
