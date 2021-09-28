package com.bottomupquestionphd.demo.services.deleteservice;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.answers.TextAnswerVote;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.MultipleAnswerQuestion;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.bottomupquestionphd.demo.domains.daos.questions.QuestionTextPossibility;
import com.bottomupquestionphd.demo.domains.daos.questions.QuestionType;
import org.springframework.stereotype.Service;

@Service
public class DeleteServiceImpl implements DeleteService{
  @Override
  public QuestionForm setQuestionFormToBeDeleted(QuestionForm questionForm) {
    questionForm.setDeleted(true);
    for (int i = 0; i <questionForm.getAnswerForms().size(); i++) {
      setAnswerFormToBeDeleted(questionForm.getAnswerForms().get(i));
    }
    return questionForm;
  }

  @Override
  public AnswerForm setAnswerFormToBeDeleted(AnswerForm answerForm) {
    answerForm.setDeleted(true);
    for (int i = 0; i <answerForm.getAnswers().size(); i++) {
      setAnswerToBeDeleted(answerForm.getAnswers().get(i));
    }
    return answerForm;
  }

  @Override
  public Question setQuestionToBeDeleted(Question question) {
    question.setDeleted(true);
    for (int i = 0; i <question.getAnswers().size(); i++) {
      setAnswerToBeDeleted(question.getAnswers().get(i));
    }
    if (question.getDiscriminatorValue().equals(QuestionType.CHECKBOXQUESTION.toString()) || question.getDiscriminatorValue().equals(QuestionType.RADIOBUTTONQUESTION.toString())){
      MultipleAnswerQuestion multipleAnswerQuestion = (MultipleAnswerQuestion) question;
      for (int i = 0; i <multipleAnswerQuestion.getQuestionTextPossibilities().size(); i++) {
        setQuestionTextPossibilityToBeDeleted(multipleAnswerQuestion.getQuestionTextPossibilities().get(i));
      }
    }
    return question;
  }

  @Override
  public QuestionTextPossibility setQuestionTextPossibilityToBeDeleted(QuestionTextPossibility questionTextPossibility) {
    questionTextPossibility.setDeleted(true);
    return questionTextPossibility;
  }

  @Override
  public Answer setAnswerToBeDeleted(Answer answer) {
    answer.setDeleted(true);
    for (int i = 0; i <answer.getActualAnswerTexts().size(); i++) {
      setActualAnswerTextToBeDeleted(answer.getActualAnswerTexts().get(i));
    }
    return answer;
  }

  @Override
  public ActualAnswerText setActualAnswerTextToBeDeleted(ActualAnswerText actualAnswerText) {
    actualAnswerText.setDeleted(true);
    return actualAnswerText;
  }

  @Override
  public TextAnswerVote setTextAnswerVoteToBeDeleted(TextAnswerVote textAnswerVote) {
    textAnswerVote.setDeleted(true);
    return textAnswerVote;
  }
}
