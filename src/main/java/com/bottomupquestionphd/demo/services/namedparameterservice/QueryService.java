package com.bottomupquestionphd.demo.services.namedparameterservice;

import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormNotFilledOutByUserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QueryService {

    List<QuestionFormNotFilledOutByUserDTO> findAllQuestionFormNotFilledOutByUser(List<Long> ids);

    void deleteQuestionsBelongingToQuestionForm(long questionFormId);
}
