package com.bottomupquestionphd.demo.services.deleteservice;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.answers.TextAnswerVote;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.bottomupquestionphd.demo.domains.daos.questions.QuestionTextPossibility;
import org.springframework.stereotype.Service;

@Service
public interface DeleteService {

  QuestionForm setQuestionFormToBeDeleted(QuestionForm questionForm);

  AnswerForm setAnswerFormToBeDeleted(AnswerForm answerForm);

  Question setQuestionToBeDeleted(Question question);

  QuestionTextPossibility setQuestionTextPossibilityToBeDeleted(QuestionTextPossibility questionTextPossibility);

  Answer setAnswerToBeDeleted(Answer answer);

  ActualAnswerText setActualAnswerTextToBeDeleted(ActualAnswerText actualAnswerText);

  TextAnswerVote setTextAnswerVoteToBeDeleted(TextAnswerVote textAnswerVote);
}
