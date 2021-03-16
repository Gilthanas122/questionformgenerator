package com.bottomupquestionphd.demo.domains.daos.answers;

import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "answers")
@Where(clause = "deleted=0")
public class Answer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private boolean deleted;

  @OneToMany(mappedBy = "answer", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonManagedReference
  private List<ActualAnswerText> actualAnswerTexts = new ArrayList<>();

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonBackReference
  private AnswerForm answerForm;

  @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JsonBackReference
  private Question question;

  public Answer() {
  }

  public Answer(List<ActualAnswerText> actualAnswerTexts) {
    this.actualAnswerTexts = actualAnswerTexts;
  }

  public Answer(ActualAnswerText actualAnswerText) {
    this.actualAnswerTexts.add(actualAnswerText);
  }

  public Answer(long id, List<ActualAnswerText> actualAnswerTexts, AnswerForm answerForm, Question question) {
    this.id = id;
    this.actualAnswerTexts = actualAnswerTexts;
    this.answerForm = answerForm;
    this.question = question;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public List<ActualAnswerText> getActualAnswerTexts() {
    return actualAnswerTexts;
  }

  public void setActualAnswerTexts(List<ActualAnswerText> actualAnswerTexts) {
    this.actualAnswerTexts = actualAnswerTexts;
  }

  public AnswerForm getAnswerForm() {
    return answerForm;
  }

  public void setAnswerForm(AnswerForm answerForm) {
    this.answerForm = answerForm;
  }

  public Question getQuestion() {
    return question;
  }

  public void setQuestion(Question question) {
    this.question = question;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }
}
