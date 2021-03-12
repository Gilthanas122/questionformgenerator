package com.bottomupquestionphd.demo.services.answers;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.MissingUserException;
import com.bottomupquestionphd.demo.exceptions.questionform.QuestionFormNotFoundException;
import com.bottomupquestionphd.demo.repositories.AnswerRepository;
import com.bottomupquestionphd.demo.services.actualanswertexts.ActualAnswerTextService;
import com.bottomupquestionphd.demo.services.namedparameterservice.QueryService;
import com.bottomupquestionphd.demo.services.questions.QuestionFormService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        for (int i = 0; i < questionForm.getQuestions().size(); i++) {
            answers.get(i).setQuestion(questionForm.getQuestions().get(i));
            answerRepository.saveAndFlush(answers.get(i));
        }
    }

    @Override
    public void connectAnswersToActualAnswers(List<Answer> answers) {
        for (int i = 0; i < answers.size(); i++) {
            Answer currentAnswer = answers.get(i);
            for (int j = 0; j < answers.get(i).getActualAnswerTexts().size(); j++) {
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

    @Override
    public List<Answer> setAnswersToAnswerForm(AnswerForm answerForm, List<Answer> answers, List<Answer> originalAnswerFormsAnswers) {
        for (int i = 0; i < answers.size(); i++) {
            answers.get(i).setAnswerForm(answerForm);
            answers.get(i).setQuestion(originalAnswerFormsAnswers.get(i).getQuestion());
            answers.get(i).setId(originalAnswerFormsAnswers.get(i).getId());
        }
        answers = actualAnswerTextService.setActualAnwerTextsToAnswer(answers, originalAnswerFormsAnswers);
        return answers;
    }

    @Override
    public List<Answer> findAllAnswerTextsBelongingToAQuestion(List<Long> questionIds) {
        return answerRepository.findAllByQuestionIds(questionIds);

    }

    @Override
    public List<Answer> removeOwnAATextsFromAATToBeVoted(long appUserId, List<Answer> allActualAnswerTextsBelongingToAQuestion) {
        return
                allActualAnswerTextsBelongingToAQuestion
                    .stream()
                    .filter(a -> a.getAnswerForm().getAppUser().getId() != appUserId)
                    .collect(Collectors.toList());
    }

}
