package com.bottomupquestionphd.demo.services.actualanswertexts;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.repositories.ActualAnswerTextRepository;
import org.springframework.stereotype.Service;

@Service
public class ActualAnswerTextServiceImpl implements ActualAnswerTextService {
    private final ActualAnswerTextRepository actualAnswerTextRepository;

    public ActualAnswerTextServiceImpl(ActualAnswerTextRepository actualAnswerTextRepository) {
        this.actualAnswerTextRepository = actualAnswerTextRepository;
    }

    @Override
    public void setToDeleted(long answerId) {
        actualAnswerTextRepository.setElementsToDeleted(answerId);
    }

    @Override
    public void saveActualAnswer(ActualAnswerText actualAnswerText) {
        this.actualAnswerTextRepository.save(actualAnswerText);
    }
}
