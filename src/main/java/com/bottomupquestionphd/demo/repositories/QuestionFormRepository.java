package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUsersQuestionFormsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionFormRepository extends JpaRepository<QuestionForm, Long> {
  boolean existsByName(String name);

  @Query("SELECT q from QuestionForm q where q.appUser.id = :id")
  List<QuestionForm> findAllByAppUserId(long id);

  @Query("SELECT q.name, q.id from QuestionForm q where q.appUser.id = ?1")
  List<AppUsersQuestionFormsDTO> findAllbyAppUserIdSelectTitleAndId(long id);
}
