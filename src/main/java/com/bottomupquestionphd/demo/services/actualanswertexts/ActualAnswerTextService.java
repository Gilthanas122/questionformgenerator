package com.bottomupquestionphd.demo.services.actualanswertexts;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ActualAnswerTextService {

    void setToDeleted(List<Long> answerIds);

    void saveActualAnswer(ActualAnswerText actualAnswerText);

    List<Answer> setActualAnwerTextsToAnswer(List<Answer> answers, List<Answer> originalAnswerFormsAnswers);
}
