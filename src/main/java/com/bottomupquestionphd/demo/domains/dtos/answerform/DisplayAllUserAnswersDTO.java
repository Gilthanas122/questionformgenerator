package com.bottomupquestionphd.demo.domains.dtos.answerform;

import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionTypesAndQuestionTextsSizeMissMatchException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DisplayAllUserAnswersDTO {
  private List<String> questionTextsAndTypes = new ArrayList<>();
  private List<List<String>> answers = new ArrayList<>();

  public DisplayAllUserAnswersDTO() {
  }

  public DisplayAllUserAnswersDTO(QuestionForm questionForm, List<List<String>> answerTexts) throws QuestionTypesAndQuestionTextsSizeMissMatchException {
    create(questionForm, answerTexts);
  }

  private void create(QuestionForm questionForm, List<List<String>> answerTexts) throws QuestionTypesAndQuestionTextsSizeMissMatchException {
    this.answers.addAll(answerTexts);
    this.questionTextsAndTypes.addAll(createQuestionTextsAndTypes(questionForm.getQuestionTexts(), questionForm.getQuestionTypes()));
  }

  private Collection<String> createQuestionTextsAndTypes(List<String> questionTexts, List<String> questionTypes) throws QuestionTypesAndQuestionTextsSizeMissMatchException {
    List<String> questionTextsAndTypes = new ArrayList<>();
    if (questionTexts.size() != questionTypes.size()) {
      throw new QuestionTypesAndQuestionTextsSizeMissMatchException("The number of questions and the number of questionTypes should be equal");
    }
    for (int i = 0; i < questionTexts.size(); i++) {
      StringBuilder temp = new StringBuilder();
      temp.append(questionTexts.get(i)).append(" (Type ").append(questionTypes.get(i)).append(" )");
      questionTextsAndTypes.add(temp.toString());
    }
    return questionTextsAndTypes;
  }

  public List<String> getQuestionTextsAndTypes() {
    return questionTextsAndTypes;
  }

  public void setQuestionTextsAndTypes(List<String> questionTextsAndTypes) {
    this.questionTextsAndTypes = questionTextsAndTypes;
  }

  public List<List<String>> getAnswers() {
    return answers;
  }

  public void setAnswers(List<List<String>> answers) {
    this.answers = answers;
  }
}
