package com.bottomupquestionphd.demo.domains.dtos.textanswervote;

import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;

import java.util.ArrayList;
import java.util.List;

public class TextAnswerVoteForVotingDTO {
  private long questionFormId;
  private long answerFormId;
  private long appUserId;
  private List<Question> questions = new ArrayList<>();
  private List<Answer> answers = new ArrayList<>();

  public TextAnswerVoteForVotingDTO() {
  }

  public TextAnswerVoteForVotingDTO(long questionFormId, long answerFormId, long appUserId, List<Question> questions, List<Answer> answers) {
    this.questionFormId = questionFormId;
    this.answerFormId = answerFormId;
    this.appUserId = appUserId;
    this.questions = questions;
    this.answers = answers;
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

  public List<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }
}