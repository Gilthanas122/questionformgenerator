package com.bottomupquestionphd.demo.domains.daos.questions;

import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "questions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Question_Type")
public class Question implements Comparable<Question> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String questionText;
  private Integer listPosition;

  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private QuestionForm questionForm;

  @OneToMany(mappedBy = "question", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private List<Answer> answers = new ArrayList<>();

  public Question(){}

  public Question(String questionText) {
    this.questionText = questionText;
  }

  public Question(long id, String questionText) {
    this.id = id;
    this.questionText = questionText;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getQuestionText() {
    return questionText;
  }

  public void setQuestionText(String questionText) {
    this.questionText = questionText;
  }

  public QuestionForm getQuestionForm() {
    return questionForm;
  }

  public void setQuestionForm(QuestionForm questionForm) {
    this.questionForm = questionForm;
  }

  public Integer getListPosition() {
    return listPosition;
  }

  public void setListPosition(Integer listPosition) {
    this.listPosition = listPosition;
  }

  @Override
  public int compareTo(Question o) {
    return this.getListPosition().compareTo(o.getListPosition());
  }


  @Transient
  public String getDiscriminatorValue() {
    return this.getClass().getAnnotation(DiscriminatorValue.class).value();
  }

  public List<String> getAnswerPossibilitiesTexts(){
    return null;
  }

  public List<Answer> getAnswers() {
    return answers;
  }

  public void setAnswers(List<Answer> answers) {
    this.answers = answers;
  }
}
