package com.bottomupquestionphd.demo.domains.dtos.answerform;

import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;

import java.util.List;

public interface QuestionAndAnswersForUpdatingAnswerFormDTO {
  List<Question> getQuestions();

  List<Answer> getAnswers();
}
