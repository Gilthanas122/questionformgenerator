package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ActualAnswerTextRepository extends JpaRepository<ActualAnswerText, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ActualAnswerText m SET m.deleted = 1 WHERE m.answer.id = :answerId")
    void setElementsToDeleted(@Param("answerId") long answerId);
}
