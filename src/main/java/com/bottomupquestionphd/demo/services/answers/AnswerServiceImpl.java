package com.bottomupquestionphd.demo.services.answers;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUsersQuestionFormsDTO;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.appuser.NoSuchUserByIdException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.AnswerRepository;
import com.bottomupquestionphd.demo.services.actualanswertexts.ActualAnswerTextService;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final QuestionFormService questionFormService;
    private final ActualAnswerTextService actualAnswerTextService;

    public AnswerServiceImpl(AnswerRepository answerRepository, QuestionFormService questionFormService, ActualAnswerTextService actualAnswerTextService) {
        this.answerRepository = answerRepository;
        this.questionFormService = questionFormService;
        this.actualAnswerTextService = actualAnswerTextService;
    }

    @Override
    public void connectQuestionsToAnswers(List<Answer> answers, long questionFormId) throws MissingUserException, QuestionFormNotFoundException, BelongToAnotherUserException {
        QuestionForm questionForm = questionFormService.findByIdForAnswerForm(questionFormId);

        for (int i = 0; i <questionForm.getQuestions().size() ; i++) {
            answers.get(i).setQuestion(questionForm.getQuestions().get(i));
            answerRepository.saveAndFlush(answers.get(i));
        }
    }

    @Override
    public void connectAnswersToActualAnswers(List<Answer> answers) {
        for (int i = 0; i <answers.size() ; i++) {
            Answer currentAnswer = answers.get(i);
            for (int j = 0; j <answers.get(i).getActualAnswerTexts().size() ; j++) {
                ActualAnswerText currentActualAnswer = currentAnswer.getActualAnswerTexts().get(j);
                currentActualAnswer.setAnswer(currentAnswer);
                actualAnswerTextService.saveActualAnswer(currentActualAnswer);
            }
        }
    }

    @Override
    public void setActualAnswersToDeleted(long appUserId, long questionFormId) {
        List<Long> answerIdsToBeDeleted = answerRepository.findAllAnswerToBeDeleted(appUserId);
        answerRepository.setAnswerToDeletedByQuestionFormId(questionFormId);
        actualAnswerTextService.setToDeleted(answerIdsToBeDeleted);
    }

}