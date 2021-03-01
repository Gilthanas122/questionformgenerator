package com.bottomupquestionphd.demo.domains.dtos.answerform;

import com.bottomupquestionphd.demo.domains.daos.questions.Question;

import java.util.ArrayList;
import java.util.List;


public class CreateAnswerFormDTO {
  private long questionFormId;
  private long answerFormId;
  private List<Question> questions = new ArrayList<>();

  public CreateAnswerFormDTO() {
  }

  public CreateAnswerFormDTO(long questionFormId, long answerFormId, List<Question> questions) {
    this.questionFormId = questionFormId;
    this.answerFormId = answerFormId;
    this.questions = questions;
  }

  public long getQuestionFormId() {
    return questionFormId;
  }

  public void setQuestionFormId(long questionFormId) {
    this.questionFormId = questionFormId;
  }

  public long getAnswerFormId() {
    return answerFormId;
  }

  public void setAnswerFormId(long answerFormId) {
    this.answerFormId = answerFormId;
  }

  public List<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }
}
