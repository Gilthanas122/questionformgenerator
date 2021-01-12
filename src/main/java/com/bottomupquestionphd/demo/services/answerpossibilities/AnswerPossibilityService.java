package com.bottomupquestionphd.demo.services.answerpossibilities;

import com.bottomupquestionphd.demo.domains.daos.questions.AnswerPossibility;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AnswerPossibilityService {
  List<AnswerPossibility> converStringsToAnswerPossibilities(List<String> answers);
}
