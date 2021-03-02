package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.answers.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
