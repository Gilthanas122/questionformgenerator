package com.bottomupquestionphd.demo.services.actualanswertexts;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.repositories.ActualAnswerTextRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActualAnswerTextServiceImpl implements ActualAnswerTextService {
    private final ActualAnswerTextRepository actualAnswerTextRepository;

    public ActualAnswerTextServiceImpl(ActualAnswerTextRepository actualAnswerTextRepository) {
        this.actualAnswerTextRepository = actualAnswerTextRepository;
    }

    @Override
    public void setToDeleted(List<Long> answerIds) {
        for (int i = 0; i < answerIds.size(); i++) {
            actualAnswerTextRepository.setElementsToDeleted(answerIds.get(i));
        }
    }

    @Override
    public void saveActualAnswer(ActualAnswerText actualAnswerText) {
        this.actualAnswerTextRepository.saveAndFlush(actualAnswerText);
    }
}
