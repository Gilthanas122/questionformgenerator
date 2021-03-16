package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.questions.QuestionTextPossibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTextPossibilityRepository extends JpaRepository<QuestionTextPossibility, Long> {
}
