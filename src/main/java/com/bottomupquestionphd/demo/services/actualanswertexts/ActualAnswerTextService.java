package com.bottomupquestionphd.demo.services.actualanswertexts;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActualAnswerTextService {

    void setToDeleted(List<Long> answerIds);

    void saveActualAnswer(ActualAnswerText actualAnswerText);
}
