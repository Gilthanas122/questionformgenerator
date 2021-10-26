package com.bottomupquestionphd.demo.domains.dtos.appuser;

public class AppUserQuestionFormsBelongsToUserDTO implements Comparable<AppUserQuestionFormsBelongsToUserDTO> {
  private long questionFormId;
  private String name;
  private long answerFormId;
  private long appUserId;
  private Boolean filledOutByUser;

  public AppUserQuestionFormsBelongsToUserDTO() {
  }

  public AppUserQuestionFormsBelongsToUserDTO(long questionFormId, String name, long answerFormId, long appUserId) {
    this.questionFormId = questionFormId;
    this.name = name;
    this.answerFormId = answerFormId;
    this.appUserId = appUserId;
  }

  public long getQuestionFormId() {
    return questionFormId;
  }

  public void setQuestionFormId(long questionFormId) {
    this.questionFormId = questionFormId;
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

  public long getAppUserId() {
    return appUserId;
  }

  public void setAppUserId(long appUserId) {
    this.appUserId = appUserId;
  }

  public boolean isFilledOutByUser() {
    return filledOutByUser;
  }

  public void setFilledOutByUser(boolean filledOutByUser) {
    this.filledOutByUser = filledOutByUser;
  }

  @Override
  public int compareTo(AppUserQuestionFormsBelongsToUserDTO o) {
    return this.filledOutByUser.compareTo(o.filledOutByUser);
  }
}
