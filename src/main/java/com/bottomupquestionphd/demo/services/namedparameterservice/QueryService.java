package com.bottomupquestionphd.demo.services.namedparameterservice;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormNotFilledOutByUserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QueryService {

  List<QuestionFormNotFilledOutByUserDTO> findAllQuestionFormNotFilledOutByUser(List<Long> ids);

  void deleteQuestionsBelongingToQuestionForm(long questionFormId);

  List<String> filterActualAnswerTextsForScaleQuestion(long id, String operator, String searchTerm);

  List<ActualAnswerText> findActualAnswerTexts(List<Long> actualAnswerTextIds);
}
