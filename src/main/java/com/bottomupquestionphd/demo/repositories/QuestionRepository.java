package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.questions.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

  @Query("SELECT q from Question q WHERE q.id = ?1")
  Question findById(long questionId);
}
