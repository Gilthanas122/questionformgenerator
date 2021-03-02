package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ActualAnswerTextRepository extends JpaRepository<ActualAnswerText, Long> {

    @Query("UPDATE ActualAnswerText m SET m.deleted = true WHERE m.answer.id = ?1")
    void setElementsToDeleted(long answerId);
}
