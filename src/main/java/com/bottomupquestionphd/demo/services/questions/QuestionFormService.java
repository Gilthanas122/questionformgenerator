package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUsersQuestionFormsDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormCreateDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserNameException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionChangeException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionException;
import com.bottomupquestionphd.demo.exceptions.questionform.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionFormService {
  long save(QuestionForm questionForm) throws MissingParamsException, QuestionFormNameAlreadyExistsException, NoSuchUserNameException;

  QuestionForm findById(long id) throws QuestionFormNotFoundException, BelongToAnotherUserException, MissingUserException;

  List<QuestionForm> findAll() throws NoQuestionFormsInDatabaseException;

  void updateQuestionForm(QuestionForm questionForm) throws QuestionFormNotFoundException, BelongToAnotherUserException;

  void finishQuestionForm(long id) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, NotEnoughQuestionsToCreateFormException;

  List<QuestionWithDTypeDTO> findByIdAndAddQuestionType(long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException;

  Question findQuestionToSwitchPositionWith(QuestionForm questionForm, int currentPosition, String direction) throws InvalidQuestionPositionException, InvalidQuestionPositionChangeException;

  void updateQuestionListPositionAfterDeletingQuestion(QuestionForm questionForm);

  QuestionForm findByIdForAnswerForm(long questionFormId) throws QuestionFormNotFoundException, MissingUserException;

    List<AppUsersQuestionFormsDTO> findQuestionFormsByAppUserId(long appUserId) throws NoSuchUserByIdException, BelongToAnotherUserException;

    void deleteQuestionForm(long questionFormId) throws QuestionFormNotFoundException;
}
