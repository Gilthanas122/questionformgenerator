package com.bottomupquestionphd.demo.services.answerforms;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.dtos.answerform.CreateAnswerFormDTO;
import com.bottomupquestionphd.demo.domains.dtos.answerform.DisplayAllUserAnswersDTO;
import com.bottomupquestionphd.demo.domains.dtos.answerform.DisplayAnswersFromAnAnswerFormDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUsersQuestionFormsDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.answer.AnswerNotFoundByIdException;
import com.bottomupquestionphd.demo.exceptions.answerform.*;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionTypesAndQuestionTextsSizeMissMatchException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnswerFormService {
  CreateAnswerFormDTO createAnswerFormDTO(long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, AnswerFormAlreadyFilledOutByCurrentUserException;

  AnswerForm saveAnswerForm(AnswerForm answerForm, long questionFormId, long appUserId) throws NoSuchUserByIdException, MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException, AnswerFormAlreadyFilledOutByCurrentUserException;

  AnswerForm findAnswerFormById(long answerFormId) throws NoSuchAnswerformById;

  List<AppUsersQuestionFormsDTO> findQuestionFormsFilledOutByAppUserId(long appUserId) throws BelongToAnotherUserException;

  List<Long> findQuestionFormIdsFilledOutByUser(long appUserId);

  CreateAnswerFormDTO createAnswerFormToUpdate(long questionFormId, long appUserId) throws BelongToAnotherUserException, QuestionFormNotFoundException, MissingUserException, AnswerFormNotFilledOutException, MissingParamsException;

  AnswerForm saveUpdatedAnswerForm(long answerFormId, long appUserId, AnswerForm answerForm) throws NoSuchUserByIdException, MissingParamsException, NoSuchAnswerformById, BelongToAnotherUserException, NumberOfQuestionAndAnswersShouldMatchException, AnswerFormNumberOfAnswersShouldMatchException, AnswerFormNotFoundException;

  DisplayAnswersFromAnAnswerFormDTO findAnswerFormByAnswerId(long answerId) throws AnswerNotFoundByIdException, NoSuchAnswerformById, BelongToAnotherUserException;

  DisplayAllUserAnswersDTO findAllAnswersBelongingToQuestionForm(long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, NoUserFilledOutAnswerFormException, QuestionTypesAndQuestionTextsSizeMissMatchException;

}
