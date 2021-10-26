package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

  @Transactional
  @Modifying
  @Query("UPDATE Answer a SET a.deleted = 1 WHERE a.answerForm.id = ?1")
  void setAnswerToDeletedByQuestionFormId(long questionFormId);

  @Query("SELECT a.id FROM Answer a WHERE a.answerForm.id = ?1")
  List<Long> findAllAnswerToBeDeleted(long appUserId);

  @Query("SELECT a.actualAnswerTexts from Answer a WHERE a.question.id = ?1")
  List<ActualAnswerText> findAllAnswerBelongingToAQuestion(long questionId);

  @Query("SELECT a from Answer a WHERE a.question.id IN ?1")
  List<Answer> findAllByQuestionIds(List<Long> questionIds);

}
