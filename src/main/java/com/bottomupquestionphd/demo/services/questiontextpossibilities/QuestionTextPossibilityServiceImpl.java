package com.bottomupquestionphd.demo.services.questiontextpossibilities;

import com.bottomupquestionphd.demo.domains.daos.questions.QuestionTextPossibility;
import com.bottomupquestionphd.demo.repositories.QuestionTextPossibilityRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionTextPossibilityServiceImpl implements QuestionTextPossibilityService {

  private final QuestionTextPossibilityRepository questionTextPossibilityRepository;

  public QuestionTextPossibilityServiceImpl(QuestionTextPossibilityRepository questionTextPossibilityRepository) {
    this.questionTextPossibilityRepository = questionTextPossibilityRepository;
  }

  @Override
  public List<QuestionTextPossibility> convertStringToQuestionTextPossibility(List<String> questionTextPossibilities) {
   return questionTextPossibilities
    .stream()
    .map(questionTextPossibility -> new QuestionTextPossibility(questionTextPossibility))
    .collect(Collectors.toList());
  }
}
