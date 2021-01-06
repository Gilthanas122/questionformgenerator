package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.QuestionForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionFormRepository extends JpaRepository<QuestionForm, Long> {
}
