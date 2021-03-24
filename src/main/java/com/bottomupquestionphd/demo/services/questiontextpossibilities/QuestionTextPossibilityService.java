package com.bottomupquestionphd.demo.services.questiontextpossibilities;

import com.bottomupquestionphd.demo.domains.daos.questions.QuestionTextPossibility;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QuestionTextPossibilityService {

  List<QuestionTextPossibility> convertStringToQuestionTextPossibility(List<String> answers);
}
