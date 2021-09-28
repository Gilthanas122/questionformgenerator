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
      questionWithDTypeDTO.setQuestionTextPossibilities(multipleAnswerQuestion.getQuestionTextPossibilities());
      if (question instanceof RadioButtonQuestion){
        questionWithDTypeDTO.setQuestionType(QuestionType.CHECKBOXQUESTION.toString());
      }else{
        questionWithDTypeDTO.setQuestionType(QuestionType.CHECKBOXQUESTION.toString());
      }
    }else if (question instanceof ScaleQuestion){
      ScaleQuestion scaleQuestion = (ScaleQuestion) question;
      questionWithDTypeDTO.setScale(scaleQuestion.getScale());
      questionWithDTypeDTO.setQuestionType(QuestionType.CHECKBOXQUESTION.toString());
    }else{
      questionWithDTypeDTO.setQuestionType(QuestionType.TEXTQUESTION.toString());
    }
    return questionWithDTypeDTO;
  }

  @Override
  public Question convertQuestionWithDTypeToQuestion(QuestionWithDTypeDTO questionWithDType) {
    if (questionWithDType.getQuestionType().equals(QuestionType.CHECKBOXQUESTION.toString())){
      CheckBoxQuestion checkBoxQuestion = new CheckBoxQuestion(questionWithDType.getId(), questionWithDType.getQuestionText(), questionWithDType.getQuestionTextPossibilities());
      checkBoxQuestion = (CheckBoxQuestion)  setQuestionToAnswerPossibilities(checkBoxQuestion);
      return checkBoxQuestion;
    }else if (questionWithDType.getQuestionType().equals(QuestionType.CHECKBOXQUESTION.toString())){
      RadioButtonQuestion radioButtonQuestion = new RadioButtonQuestion(questionWithDType.getId(), questionWithDType.getQuestionText(), questionWithDType.getQuestionTextPossibilities());
      radioButtonQuestion = (RadioButtonQuestion) setQuestionToAnswerPossibilities(radioButtonQuestion);
      return radioButtonQuestion;
    }else if (questionWithDType.getQuestionType().equals(QuestionType.SCALEQUESTION.toString())){
      ScaleQuestion scaleQuestion = new ScaleQuestion(questionWithDType.getId(), questionWithDType.getQuestionText(), questionWithDType.getScale());
      return scaleQuestion;
    }
    return new TextQuestion(questionWithDType.getId(), questionWithDType.getQuestionText());
  }

  private MultipleAnswerQuestion setQuestionToAnswerPossibilities(MultipleAnswerQuestion multipleAnswerQuestion) {
    for (QuestionTextPossibility questionTextPossibility : multipleAnswerQuestion.getQuestionTextPossibilities()) {
      questionTextPossibility.setMultipleAnswerQuestion(multipleAnswerQuestion);
    }
    return multipleAnswerQuestion;
  }

}
