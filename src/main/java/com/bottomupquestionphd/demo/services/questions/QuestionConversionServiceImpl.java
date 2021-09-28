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
        questionWithDTypeDTO.setQuestionType(QuestionType.RADIOBUTTONQUESTION.toString());
      }else{
        questionWithDTypeDTO.setQuestionType(QuestionType.CHECKBOXQUESTION.toString());
      }
    }else if (question instanceof ScaleQuestion){
      ScaleQuestion scaleQuestion = (ScaleQuestion) question;
      questionWithDTypeDTO.setScale(scaleQuestion.getScale());
      questionWithDTypeDTO.setQuestionType(QuestionType.SCALEQUESTION.toString());
    }else{
      questionWithDTypeDTO.setQuestionType(QuestionType.TEXTQUESTION.toString());
    }
    return questionWithDTypeDTO;
  }

  @Override
  public Question convertQuestionWithDTypeToQuestion(QuestionWithDTypeDTO questionWithDType) {

    if (questionWithDType.getQuestionType().equals(QuestionType.CHECKBOXQUESTION.toString())){
      CheckBoxQuestion checkBoxQuestion = new CheckBoxQuestion(questionWithDType.getId(), questionWithDType.getQuestionText(), questionWithDType.getQuestionTextPossibilities());
      setQuestionToAnswerPossibilities(checkBoxQuestion);
      return checkBoxQuestion;
    }else if (questionWithDType.getQuestionType().equals(QuestionType.RADIOBUTTONQUESTION.toString())){
      RadioButtonQuestion radioButtonQuestion = new RadioButtonQuestion(questionWithDType.getId(), questionWithDType.getQuestionText(), questionWithDType.getQuestionTextPossibilities());
      setQuestionToAnswerPossibilities(radioButtonQuestion);
      return radioButtonQuestion;
    }else if (questionWithDType.getQuestionType().equals(QuestionType.SCALEQUESTION.toString())){
      return new ScaleQuestion(questionWithDType.getId(), questionWithDType.getQuestionText(), questionWithDType.getScale());
    }
    return new TextQuestion(questionWithDType.getId(), questionWithDType.getQuestionText());
  }

  private void setQuestionToAnswerPossibilities(MultipleAnswerQuestion multipleAnswerQuestion) {
    for (QuestionTextPossibility questionTextPossibility : multipleAnswerQuestion.getQuestionTextPossibilities()) {
      questionTextPossibility.setMultipleAnswerQuestion(multipleAnswerQuestion);
    }
  }

}
