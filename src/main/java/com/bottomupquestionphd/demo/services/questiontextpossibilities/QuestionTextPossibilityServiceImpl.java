package com.bottomupquestionphd.demo.services.questiontextpossibilities;

import com.bottomupquestionphd.demo.domains.daos.questions.QuestionTextPossibility;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.services.validations.ErrorServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.bottomupquestionphd.demo.exceptions.functionalinterfaces.ThrowingConsumer.throwingConsumerWrapper;

@Service
public class QuestionTextPossibilityServiceImpl implements QuestionTextPossibilityService {

  @Override
  public List<QuestionTextPossibility> convertStringToQuestionTextPossibility(List<String> questionTextPossibilities) {
    questionTextPossibilities
            .forEach(throwingConsumerWrapper(ErrorServiceImpl::buildMissingFieldErrorMessage, MissingParamsException.class));

    return questionTextPossibilities
            .stream()
            .filter(questionTextPossibility -> questionTextPossibility != null && !questionTextPossibility.equals(""))
            .map(questionTextPossibility -> new QuestionTextPossibility(questionTextPossibility))
            .collect(Collectors.toList());
  }
}

