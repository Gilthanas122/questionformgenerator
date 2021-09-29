package com.bottomupquestionphd.demo.services.actualanswertexts;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActualAnswerTextService {

  void setToDeleted(List<Long> answerIds) throws MissingParamsException;

  void saveActualAnswer(ActualAnswerText actualAnswerText) throws MissingParamsException;

  List<Answer> setActualAnswerTextsToAnswer(List<Answer> answers, List<Answer> originalAnswerFormsAnswers) throws MissingParamsException;

  void setAnswerTextsToBeDeleted(List<Long> answerIds);
}
