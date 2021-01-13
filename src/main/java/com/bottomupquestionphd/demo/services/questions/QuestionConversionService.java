package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import org.springframework.stereotype.Service;

@Service
public interface QuestionConversionService {

 QuestionWithDTypeDTO convertFromQuestionToQuestionWithDType(Question question);

 Question convertQuestionWithDTypeToQuestion(QuestionWithDTypeDTO question);
  }
