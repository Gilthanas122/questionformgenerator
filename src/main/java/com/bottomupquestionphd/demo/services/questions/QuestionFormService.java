package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormCreateDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserNameException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionChangeException;
import com.bottomupquestionphd.demo.exceptions.question.InvalidQuestionPositionException;
import com.bottomupquestionphd.demo.exceptions.questionform.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionFormService {
  long save(QuestionFormCreateDTO questionFormCreateDTO) throws MissingParamsException, QuestionFormNameAlreadyExistsException, NoSuchUserNameException;

  QuestionForm findById(long id) throws QuestionFormNotFoundException, BelongToAnotherUserException, MissingUserException;

  List<QuestionForm> findAll() throws NoQuestionFormsInDatabaseException;

  void finishQuestionForm(long id) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, NotEnoughQuestionsToCreateFormException;

  List<QuestionWithDTypeDTO> findByIdAndAddQuestionType(long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException;

  Question findQuestionToSwitchPositionWith(QuestionForm questionForm, int currentPosition, String direction) throws InvalidQuestionPositionException, InvalidQuestionPositionChangeException;

  void updateQuestionListPositionAfterDeletingQuestion(QuestionForm questionForm);

  QuestionForm findByIdForAnswerForm(long questionFormId) throws QuestionFormNotFoundException;

  void deleteQuestionForm(long questionFormId) throws QuestionFormNotFoundException, BelongToAnotherUserException;

  List<Long> getAllTextQuestionIdsFromQuestionForm(QuestionForm questionForm);

  void updateQuestionForm(QuestionFormCreateDTO questionFormCreateDTO, long id) throws MissingParamsException, QuestionFormNotFoundException, BelongToAnotherUserException;

  void updateAnswerFormAfterAddingNewQuestion(QuestionForm questionForm, Question question);

}
