package com.bottomupquestionphd.demo.domains.daos.answers;

import com.bottomupquestionphd.demo.domains.daos.questions.Question;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "CheckBoxAnswer")
@DiscriminatorValue("CheckBoxAnswer")
public abstract class CheckBoxAnswer extends Question {

  @OneToMany(mappedBy = "checkBoxAnswer", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  private List<CheckBoxAnswerText> checkBoxQuestionAnswerTexts = new ArrayList<>();

  public CheckBoxAnswer(){}

  public CheckBoxAnswer(String questionText, List<CheckBoxAnswerText> checkBoxQuestionAnswerTexts) {
    super(questionText);
    this.checkBoxQuestionAnswerTexts = checkBoxQuestionAnswerTexts;
  }

  public List<CheckBoxAnswerText> getMultipleAnswerTexts() {
    return checkBoxQuestionAnswerTexts;
  }

  public void setMultipleAnswerTexts(List<CheckBoxAnswerText> checkBoxQuestionAnswerTexts) {
    this.checkBoxQuestionAnswerTexts = checkBoxQuestionAnswerTexts;
  }
}
