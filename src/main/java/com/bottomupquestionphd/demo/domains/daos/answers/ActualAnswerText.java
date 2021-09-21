package com.bottomupquestionphd.demo.domains.daos.answers;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.NotNull;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "actualanswertexts")
@Where(clause="deleted=0")
public class ActualAnswerText {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @NotNull
  private String answerText;
  private boolean deleted;

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonBackReference(value = "answersActualAnswerTexts")
  private Answer answer;

  @OneToMany(mappedBy = "actualAnswerText", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonManagedReference(value = "actualAnswerTextsTextAnswerVotes")
  private List<TextAnswerVote> textAnswerVotes = new ArrayList<>();

  public ActualAnswerText() {
  }

  public ActualAnswerText(long id, String answerText) {
    this.id = id;
    this.answerText = answerText;
  }

  public ActualAnswerText(Answer answer) {
    this.answer = answer;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Answer getAnswer() {
    return answer;
  }

  public void setAnswer(Answer answer) {
    this.answer = answer;
  }

  public String getAnswerText() {
    return answerText;
  }

  public void setAnswerText(String answerText) {
    this.answerText = answerText;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public boolean actualAnswerTextIsNullOrEmpty(){
    return this.getAnswerText() == null || this.getAnswerText().isEmpty();
  }

  public List<TextAnswerVote> getTextAnswerVotes() {
    return textAnswerVotes;
  }

  public void setTextAnswerVotes(List<TextAnswerVote> textAnswerVotes) {
    this.textAnswerVotes = textAnswerVotes;
  }

  public double getAverageOfTextAnswerVotes(){
    return textAnswerVotes.stream().mapToDouble(TextAnswerVote::getValue).average().orElse(0);
  }

  public String getAnswerTextWithAverageOfTextAnswerVotes(){
    return this.answerText + "(" + getAverageOfTextAnswerVotes() + ")";
  }
}
