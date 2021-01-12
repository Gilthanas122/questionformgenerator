package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.questions.AnswerPossibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerPossibilityRepository extends JpaRepository<AnswerPossibility, Long> {
}
