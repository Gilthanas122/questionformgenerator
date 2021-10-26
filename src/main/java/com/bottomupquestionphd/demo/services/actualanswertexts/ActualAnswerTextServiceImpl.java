package com.bottomupquestionphd.demo.services.actualanswertexts;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.repositories.ActualAnswerTextRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActualAnswerTextServiceImpl implements ActualAnswerTextService {
  private final ActualAnswerTextRepository actualAnswerTextRepository;

  public ActualAnswerTextServiceImpl(ActualAnswerTextRepository actualAnswerTextRepository) {
    this.actualAnswerTextRepository = actualAnswerTextRepository;
  }

  @Override
  @Transactional
  public void setToDeleted(List<Long> answerIds) throws MissingParamsException {
    if (answerIds == null) {
      throw new MissingParamsException("Answer ids can not be null");
    }
    for (int i = 0; i < answerIds.size(); i++) {
      actualAnswerTextRepository.setElementsToDeleted(answerIds.get(i));
    }
  }

  @Override
  public void saveActualAnswer(ActualAnswerText actualAnswerText) throws MissingParamsException {
    if (actualAnswerText == null || actualAnswerText.getAnswerText().isEmpty()) {
      throw new MissingParamsException("Can not save a null actual answers text");
    }
    this.actualAnswerTextRepository.saveAndFlush(actualAnswerText);
  }

  @Override
  public List<Answer> setActualAnswerTextsToAnswer(List<Answer> answers, List<Answer> originalAnswers) throws MissingParamsException {
    if (answers == null || originalAnswers == null) {
      throw new MissingParamsException("Answers or original answers can not be null");
    }
    for (Answer answer : answers) {
      for (ActualAnswerText actualAnswerText : answer.getActualAnswerTexts()) {
        actualAnswerText.setAnswer(answer);
      }
    }
    setToDeleted(retrieveIdsFromAnswers(originalAnswers));
    return answers;
  }

  //NOT TESTED
  @Override
  public void setAnswerTextsToBeDeleted(List<Long> answerIds) throws MissingParamsException {
    if (answerIds == null) {
      throw new MissingParamsException("List of answerids can not be null");
    }
    actualAnswerTextRepository.setElementsToBeDeletedByMultipleAnswerIds(answerIds);
  }

  private List<Long> retrieveIdsFromAnswers(List<Answer> answers) {
    return answers
            .stream()
            .map(Answer::getId)
            .collect(Collectors.toList());
  }
}
