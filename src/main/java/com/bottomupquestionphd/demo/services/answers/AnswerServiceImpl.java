package com.bottomupquestionphd.demo.services.answers;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
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
        QuestionForm questionForm = questionFormService.findById(questionFormId);

        for (int i = 0; i <questionForm.getQuestions().size() ; i++) {
            answers.get(i).setQuestion(questionForm.getQuestions().get(i));
        }
        answerRepository.saveAll(answers);
    }

    @Override
    public void connectAnswersToActualAnswers(List<Answer> answers) {
        for (Answer answer: answers) {
            for (ActualAnswerText actualAnswerText: answer.getActualAnswerTexts()) {
                actualAnswerText.setAnswer(answer);
                actualAnswerTextService.saveActualAnswer(actualAnswerText);
            }
        }
    }
}
