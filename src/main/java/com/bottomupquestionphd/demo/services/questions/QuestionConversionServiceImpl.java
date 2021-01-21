package com.bottomupquestionphd.demo.services.questions;

import com.bottomupquestionphd.demo.domains.daos.questions.*;
import com.bottomupquestionphd.demo.domains.dtos.question.QuestionWithDTypeDTO;
import org.springframework.stereotype.Service;

@Service
public class QuestionConversionServiceImpl implements QuestionConversionService{


  @Override
  public QuestionWithDTypeDTO convertFromQuestionToQuestionWithDType(Question question) {
    QuestionWithDTypeDTO questionWithDTypeDTO = new QuestionWithDTypeDTO(question.getId(), question.getQuestionText());
    questionWithDTypeDTO.setQuestionFormId(question.getQuestionForm().getId());
    if (question instanceof MultipleAnswerQuestion){
      MultipleAnswerQuestion multipleAnswerQuestion = (MultipleAnswerQuestion) question;
      questionWithDTypeDTO.setAnswerPossibilities(multipleAnswerQuestion.getAnswerPossibilities());
      if (question instanceof RadioButtonQuestion){
        questionWithDTypeDTO.setQuestionType("Radio button");
      }else{
        questionWithDTypeDTO.setQuestionType("Check box");
      }
    }else if (question instanceof ScaleQuestion){
      ScaleQuestion scaleQuestion = (ScaleQuestion) question;
      questionWithDTypeDTO.setScale(scaleQuestion.getScale());
      questionWithDTypeDTO.setQuestionType("Scale");
    }else{
      questionWithDTypeDTO.setQuestionType("Text");
    }
    return questionWithDTypeDTO;
  }

  @Override
  public Question convertQuestionWithDTypeToQuestion(QuestionWithDTypeDTO questionWithDType) {
    if (questionWithDType.getQuestionType().equals("Check box")){
      CheckBoxQuestion checkBoxQuestion = new CheckBoxQuestion(questionWithDType.getId(), questionWithDType.getQuestionText(), questionWithDType.getAnswerPossibilities());
      checkBoxQuestion = (CheckBoxQuestion)  setQuestionToAnswerPossibilities(checkBoxQuestion);
      return checkBoxQuestion;
    }else if (questionWithDType.getQuestionType().equals("Radio button")){
      RadioButtonQuestion radioButtonQuestion = new RadioButtonQuestion(questionWithDType.getId(), questionWithDType.getQuestionText(), questionWithDType.getAnswerPossibilities());
      radioButtonQuestion = (RadioButtonQuestion) setQuestionToAnswerPossibilities(radioButtonQuestion);
      return radioButtonQuestion;
    }else if (questionWithDType.getQuestionType().equals("Scale")){
      ScaleQuestion scaleQuestion = new ScaleQuestion(questionWithDType.getId(), questionWithDType.getQuestionText(), questionWithDType.getScale());
      return scaleQuestion;
    }
    return new TextQuestion(questionWithDType.getId(), questionWithDType.getQuestionText());
  }

  private MultipleAnswerQuestion setQuestionToAnswerPossibilities(MultipleAnswerQuestion multipleAnswerQuestion) {
    for (AnswerPossibility answerPossibility: multipleAnswerQuestion.getAnswerPossibilities()) {
      answerPossibility.setMultipleAnswerQuestion(multipleAnswerQuestion);
    }
    return multipleAnswerQuestion;
  }

}
