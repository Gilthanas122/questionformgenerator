package com.bottomupquestionphd.demo.services.answers.answerformfilter;

import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.answerformfilter.AnswerSearchTermResultDTO;
import com.bottomupquestionphd.demo.domains.dtos.answerformfilter.SearchTermsForFilteringDTO;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionFormFilteringException;
import com.bottomupquestionphd.demo.exceptions.answerformfilter.QuestionFormHasNotQuestionsException;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public interface AnswerFormFilterService {
  QuestionForm generateSearchFields(long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, QuestionFormHasNotQuestionsException;

  AnswerSearchTermResultDTO filterAnswers(long questionFormId, SearchTermsForFilteringDTO searchTermsForFilteringDTO) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, QuestionFormFilteringException;

  void returnAllAnswersBelongingToQuestionForm(long questionFormId, HttpServletResponse response) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException, IOException;
}
