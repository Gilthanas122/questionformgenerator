package com.bottomupquestionphd.demo.services.answers;

import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.answer.AnswerNotFoundByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnswerService {
  void connectQuestionsToAnswers(List<Answer> answers, long questionFormId) throws QuestionFormNotFoundException, MissingParamsException;

  void connectAnswersToActualAnswers(List<Answer> answers) throws MissingParamsException;

  void setActualAnswersToDeleted(long appUserId, long questionFormId) throws MissingParamsException;

  List<Answer> setAnswersToAnswerForm(AnswerForm answerForm, List<Answer> answers, List<Answer> originAnswerFormsAnswers) throws MissingParamsException;

  List<Answer> findAllAnswerTextsBelongingToAQuestion(List<Long> questionId) throws MissingParamsException;

  List<Answer> removeOwnAATextsFromAATToBeVoted(long appUserId, List<Answer> allActualAnswerTextsBelongingToAQuestion) throws MissingParamsException;

  List<Answer> removeNullAnswerTextsFromAnswer(List<Answer> answers) throws MissingParamsException;

  Answer findById(long answerId) throws AnswerNotFoundByIdException;

  List<Question> addActualTextAnswersNotFilledOutByUser(List<Question> questions, long appUserId);

  void setActualAnswerTextsToBeDeletedBelongingToAnswers(List<Long> collect) throws MissingParamsException;
}
