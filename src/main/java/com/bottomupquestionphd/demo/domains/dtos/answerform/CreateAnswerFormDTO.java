package com.bottomupquestionphd.demo.domains.dtos.answerform;

import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;

import java.util.ArrayList;
import java.util.List;


public class CreateAnswerFormDTO {
  private long questionFormId;
  private long answerFormId;
  private long userId;
  private List<Question> questions = new ArrayList<>();
  private List<Answer> answers = new ArrayList<>();

  public CreateAnswerFormDTO() {
  }

  public CreateAnswerFormDTO(long answerFormId, long questionFormId, long userId, List<Question> questions, List<Answer> answers) {
    this.questionFormId = questionFormId;
    this.userId = userId;
    this.answerFormId = answerFormId;
    this.questions = questions;
    this.answers = answers;
  }

  public CreateAnswerFormDTO(long answerFormId, long questionFormId,  long userId, List<Question> questions) {
    this.questionFormId = questionFormId;
    this.answerFormId = answerFormId;
    this.userId = userId;
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

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public List<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Answer> answers) {
    this.answers = answers;
  }
}
