package com.bottomupquestionphd.demo.services.answerforms;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.answerform.CreateAnswerFormDTO;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.AnswerFormRepository;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerFormServiceImpl implements AnswerFormService {
  private final AnswerFormRepository answerFormRepository;
  private final QuestionFormService questionFormService;

  public AnswerFormServiceImpl(AnswerFormRepository answerFormRepository, QuestionFormService questionFormService) {
    this.answerFormRepository = answerFormRepository;
    this.questionFormService = questionFormService;
  }

  @Override
  public CreateAnswerFormDTO createFirstAnswerForm(long questionFormId) throws MissingUserException, QuestionFormNotFoundException {
    QuestionForm questionForm = questionFormService.findByIdForAnswerForm(questionFormId);
    AnswerForm answerForm = new AnswerForm(questionForm);
    questionForm.addAnswerForm(answerForm);
    answerForm.setQuestionForm(questionForm);
    answerFormRepository.save(answerForm);

    return new CreateAnswerFormDTO(questionForm.getName(), answerForm.getId(), questionForm.getId());
  }
}
