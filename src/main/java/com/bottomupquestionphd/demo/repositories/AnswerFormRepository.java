package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerFormRepository extends JpaRepository<AnswerForm, Long> {
}
