package com.bottomupquestionphd.demo.services.answerpossibilities;

import com.bottomupquestionphd.demo.domains.daos.questions.AnswerPossibility;
import com.bottomupquestionphd.demo.repositories.AnswerPossibilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnswerPossibilityServiceImpl implements AnswerPossibilityService{

  private final AnswerPossibilityRepository answerPossibilityRepository;

  public AnswerPossibilityServiceImpl(AnswerPossibilityRepository answerPossibilityRepository) {
    this.answerPossibilityRepository = answerPossibilityRepository;
  }

  @Override
  public List<AnswerPossibility> converStringsToAnswerPossibilities(List<String> answers) {
   return answers
    .stream()
    .map(answer -> new AnswerPossibility(answer))
    .collect(Collectors.toList());
  }
}
