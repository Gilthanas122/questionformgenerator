package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormCreateDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserNameException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.NoQuestionFormsInDatabaseException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNameAlreadyExistsException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionFormService {
  long save(QuestionForm questionForm) throws MissingParamsException, QuestionFormNameAlreadyExistsException, NoSuchUserNameException;

  QuestionForm findById(long id) throws QuestionFormNotFoundException, BelongToAnotherUserException, MissingUserException;

  List<QuestionForm> findAll() throws NoQuestionFormsInDatabaseException;

  void updateQuestionForm(QuestionForm questionForm) throws QuestionFormNotFoundException, BelongToAnotherUserException;
}
