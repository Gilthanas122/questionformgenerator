package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
