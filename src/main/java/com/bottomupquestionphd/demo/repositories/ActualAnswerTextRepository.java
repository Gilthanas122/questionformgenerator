package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ActualAnswerTextRepository extends JpaRepository<ActualAnswerText, Long> {

  @Modifying
  @Transactional
  @Query("UPDATE ActualAnswerText m SET m.deleted = 1 WHERE m.answer.id = ?1")
  void setElementsToDeleted(long answerId);

  @Modifying
  @Transactional
  @Query("UPDATE ActualAnswerText m SET m.deleted = 1 WHERE m.answer.id in ?1")
  void setElementsToBeDeletedByMultipleAnswerIds(List<Long> answerIds);
}
