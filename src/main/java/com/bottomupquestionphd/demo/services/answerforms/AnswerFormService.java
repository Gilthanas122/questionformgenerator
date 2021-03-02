package com.bottomupquestionphd.demo.services.answerforms;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.dtos.answerform.CreateAnswerFormDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.answerform.NoSuchAnswerformById;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface AnswerFormService {
    CreateAnswerFormDTO createFirstAnswerForm(long questionFormId) throws MissingUserException, QuestionFormNotFoundException, NoSuchUserByIdException, BelongToAnotherUserException;

    void saveAnswerForm(AnswerForm answerForm, long answerFormId, long questionFormId, long appUserId) throws NoSuchUserByIdException, MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException;

    AnswerForm findAnswerFormById(long answerFormId) throws NoSuchAnswerformById;

    CreateAnswerFormDTO convertAnswerFormToCreateAnswerFormDTO(long questionFormId, AnswerForm answerForm) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException;
}
