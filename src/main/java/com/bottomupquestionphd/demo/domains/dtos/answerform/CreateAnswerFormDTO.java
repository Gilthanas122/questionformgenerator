package com.bottomupquestionphd.demo.domains.dtos.answerform;

import com.bottomupquestionphd.demo.domains.daos.answers.Answer;

import java.util.ArrayList;
import java.util.List;

public class CreateAnswerFormDTO {
  private String name;
  private long answerFormId;
  private long questionFormId;
  private List<Answer> answers = new ArrayList<>();

  public CreateAnswerFormDTO() {
  }

  public CreateAnswerFormDTO(String name, long answerFormId, long questionFormId) {
    this.name = name;
    this.answerFormId = answerFormId;
    this.questionFormId = questionFormId;
  }

  public CreateAnswerFormDTO(String name, long answerFormId, long questionFormId, List<Answer> answers) {
    this.name = name;
    this.answerFormId = answerFormId;
    this.questionFormId = questionFormId;
    this.answers = answers;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getAnswerFormId() {
    return answerFormId;
  }

  public void setAnswerFormId(long answerFormId) {
    this.answerFormId = answerFormId;
  }

  public long getQuestionFormId() {
    return questionFormId;
  }

  public void setQuestionFormId(long questionFormId) {
    this.questionFormId = questionFormId;
  }

  public List<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Answer> answers) {
    this.answers = answers;
  }
}
