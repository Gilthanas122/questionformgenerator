package com.bottomupquestionphd.demo.services.actualanswertexts;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import com.bottomupquestionphd.demo.repositories.ActualAnswerTextRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ActualAnswerTextServiceImpl implements ActualAnswerTextService {
    private final ActualAnswerTextRepository actualAnswerTextRepository;

    public ActualAnswerTextServiceImpl(ActualAnswerTextRepository actualAnswerTextRepository) {
        this.actualAnswerTextRepository = actualAnswerTextRepository;
    }

    @Override
    @Transactional
    public void setToDeleted(List<Long> answerIds) {
        for (int i = 0; i < answerIds.size(); i++) {
            actualAnswerTextRepository.setElementsToDeleted(answerIds.get(i));
        }
    }

    @Override
    public void saveActualAnswer(ActualAnswerText actualAnswerText) {
        this.actualAnswerTextRepository.saveAndFlush(actualAnswerText);
    }

    @Override
    public List<Answer> setActualAnwerTextsToAnswer(List<Answer> answers, List<Answer> originalAnswers) {
        for (Answer answer:answers) {
            for (ActualAnswerText actualAnswerText: answer.getActualAnswerTexts()) {
                actualAnswerText.setAnswer(answer);
            }
        }
        setToDeleted(retrieveIdFromActualAnswerTexts(originalAnswers));
        return answers;
    }

    private List<Long> retrieveIdFromActualAnswerTexts(List<Answer> actualAnswerTexts){
        List<Long> ids = new ArrayList<>();
        for (Answer answer: actualAnswerTexts) {
           ids.add(answer.getId());
        }
        return ids;
    }
}
