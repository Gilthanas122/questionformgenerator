package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormCreateDTO;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNameAlreadyExistsException;
import org.springframework.stereotype.Service;

@Service
public interface QuestionFormService {
  long save(QuestionFormCreateDTO questionFormCreateDTO) throws MissingParamsException, QuestionFormNameAlreadyExistsException;
}
