package com.bottomupquestionphd.demo.services.answerforms;

import com.bottomupquestionphd.demo.domains.dtos.answerform.CreateAnswerFormDTO;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface AnswerFormService {
  CreateAnswerFormDTO createFirstAnswerForm(long questionFormId) throws MissingUserException, QuestionFormNotFoundException;
}
