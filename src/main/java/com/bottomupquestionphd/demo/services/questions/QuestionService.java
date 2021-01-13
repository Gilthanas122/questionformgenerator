package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionCreateDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import com.bottomupquestionphd.demo.domains.dtos.question.TextQuestionDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.question.QuestionNotFoundByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface QuestionService {
  void saveQuestion(String type, QuestionCreateDTO textQuestionDTO, long questionFormId) throws MissingParamsException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingUserException;

  QuestionWithDTypeDTO findByIdAndConvertToQuestionWithDTypeDTO(long questionId) throws QuestionNotFoundByIdException, BelongToAnotherUserException;

  long findQuestionFormIdBelongingToQuestion(long questionId) throws QuestionNotFoundByIdException;

  Question findById(long questionIq) throws QuestionNotFoundByIdException;

  void saveQuestionFromQuestionDType(QuestionWithDTypeDTO question) throws QuestionNotFoundByIdException;
}
