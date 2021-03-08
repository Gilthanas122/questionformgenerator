package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.daos.questionform.QuestionForm;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUsersQuestionFormsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerFormRepository extends JpaRepository<AnswerForm, Long> {


    //using projection https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections
    @Query("SELECT q.id AS questionFormId, q.name AS name, a.id AS answerFormId, a.appUser.id AS appUserId from AnswerForm a JOIN a.questionForm q WHERE a.appUser.id = ?1")
    List<AppUsersQuestionFormsDTO> findAllQuestionFormsFilledOutByUser(long appUserId);

}
