package com.bottomupquestionphd.demo.domains.daos.answers;

import javax.persistence.*;

@Entity
@Table(name="answers")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Answer_Type")
public abstract class Answer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  public Answer() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
