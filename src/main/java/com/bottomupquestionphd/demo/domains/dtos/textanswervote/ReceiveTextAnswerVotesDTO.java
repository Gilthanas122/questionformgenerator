package com.bottomupquestionphd.demo.domains.dtos.textanswervote;

import java.util.ArrayList;
import java.util.List;

public class ReceiveTextAnswerVotesDTO {
  private List<Long> actualAnswerTextIds = new ArrayList<>();
  private List<List<Byte>> textAnswerVotes = new ArrayList<>();

  public ReceiveTextAnswerVotesDTO() {
  }

  public ReceiveTextAnswerVotesDTO(List<Long> actualAnswerTextIds, List<List<Byte>> textAnswerVotes) {
    this.actualAnswerTextIds = actualAnswerTextIds;
    this.textAnswerVotes = textAnswerVotes;
  }

  public List<Long> getActualAnswerTextIds() {
    return actualAnswerTextIds;
  }

  public void setActualAnswerTextIds(List<Long> actualAnswerTextIds) {
    this.actualAnswerTextIds = actualAnswerTextIds;
  }

  public List<List<Byte>> getTextAnswerVotes() {
    return textAnswerVotes;
  }

  public void setTextAnswerVotes(List<List<Byte>> textAnswerVotes) {
    this.textAnswerVotes = textAnswerVotes;
  }
}
