package com.bottomupquestionphd.demo.domains.dtos.answerformfilter;

import java.util.ArrayList;
import java.util.List;

public class AnswerSearchTermResultDTO {
  private long appUserId;
  private List<ActualAnswerTextSearchTermResultDTO> actualAnswerTextSearchTermResultDTOS = new ArrayList<>();

  public AnswerSearchTermResultDTO() {
  }

  public AnswerSearchTermResultDTO(long appUserId, List<ActualAnswerTextSearchTermResultDTO> actualAnswerTextSearchTermResultDTOS) {
    this.appUserId = appUserId;
    this.actualAnswerTextSearchTermResultDTOS = actualAnswerTextSearchTermResultDTOS;
  }

  public long getAppUserId() {
    return appUserId;
  }

  public void setAppUserId(long appUserId) {
    this.appUserId = appUserId;
  }

  public List<ActualAnswerTextSearchTermResultDTO> getActualAnswerTextSearchTermResultDTOS() {
    return actualAnswerTextSearchTermResultDTOS;
  }

  public void setActualAnswerTextSearchTermResultDTOS(List<ActualAnswerTextSearchTermResultDTO> actualAnswerTextSearchTermResultDTOS) {
    this.actualAnswerTextSearchTermResultDTOS = actualAnswerTextSearchTermResultDTOS;
  }
}
