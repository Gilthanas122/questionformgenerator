package com.bottomupquestionphd.demo.domains.dtos.answerform;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;

import java.util.ArrayList;
import java.util.List;


public class CreateAnswerFormDTO {
  private long questionFormId;
  private long answerFormId;
  private long appUserId;
  private List<Question> questions = new ArrayList<>();
  private List<Answer> answers = new ArrayList<>();
  private List<ActualAnswerText> otherActualAnswerTexts = new ArrayList<>();

  public CreateAnswerFormDTO() {
  }

  public CreateAnswerFormDTO(long answerFormId, long questionFormId, long appUserId, List<Question> questions, List<Answer> answers, List<ActualAnswerText> actualAnswerTexts) {
    this.questionFormId = questionFormId;
    this.appUserId = appUserId;
    this.answerFormId = answerFormId;
    this.questions = questions;
    this.answers = answers;
    this.otherActualAnswerTexts = actualAnswerTexts;
  }


  public CreateAnswerFormDTO(long answerFormId, long questionFormId,  long appUserId, List<Question> questions) {
    this.questionFormId = questionFormId;
    this.answerFormId = answerFormId;
    this.appUserId = appUserId;
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

  public long getAppUserId() {
    return appUserId;
  }

  public void setAppUserId(long appUserId) {
    this.appUserId = appUserId;
  }

  public List<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Answer> answers) {
    this.answers = answers;
  }

  public List<ActualAnswerText> getOtherActualAnswerTexts() {
    return otherActualAnswerTexts;
  }

  public void setOtherActualAnswerTexts(List<ActualAnswerText> otherActualAnswerTexts) {
    this.otherActualAnswerTexts = otherActualAnswerTexts;
  }
}
