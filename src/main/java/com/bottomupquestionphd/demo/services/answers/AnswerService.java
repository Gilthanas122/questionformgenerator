package com.bottomupquestionphd.demo.services.answers;

import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import com.bottomupquestionphd.demo.exceptions.MissingParamsException;
import com.bottomupquestionphd.demo.exceptions.answer.AnswerNotFoundByIdException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.services.answerforms.AnswerFormServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnswerService {
  void connectQuestionsToAnswers(List<Answer> answers, long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, MissingParamsException;

  void connectAnswersToActualAnswers(List<Answer> answers) throws MissingParamsException;

  void setActualAnswersToDeleted(long appUserId, long questionFormId) throws MissingParamsException;

  List<Answer> setAnswersToAnswerForm(AnswerForm answerForm, List<Answer> answers, List<Answer> originAnswerFormsAnswers) throws MissingParamsException;

  List<Answer> findAllAnswerTextsBelongingToAQuestion(List<Long> questionId) throws MissingParamsException;

  List<Answer> removeOwnAATextsFromAATToBeVoted(long appUserId, List<Answer> allActualAnswerTextsBelongingToAQuestion) throws MissingParamsException;

  List<Answer> removeNullAnswerTextsFromAnswer(List<Answer> answers, AnswerForm answerForm, List<Answer> originalFormsAnswers, AnswerFormServiceImpl answerFormService) throws MissingParamsException;

  Answer findById(long answerId) throws AnswerNotFoundByIdException;

  List<Question> addActualTextAnswersNotFilledOutByUser(List<Question> questions, long appUserId);
}
