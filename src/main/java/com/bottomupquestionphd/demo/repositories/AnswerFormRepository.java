package com.bottomupquestionphd.demo.repositories;

import com.bottomupquestionphd.demo.domains.daos.answers.AnswerForm;
import com.bottomupquestionphd.demo.domains.dtos.answerform.QuestionAndAnswersForUpdatingAnswerFormDTO;
import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUsersQuestionFormsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerFormRepository extends JpaRepository<AnswerForm, Long> {


    //using projection https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections
    @Query("SELECT q.id AS questionFormId, q.name AS name, a.id AS answerFormId, a.appUser.id AS appUserId FROM AnswerForm a JOIN a.questionForm q WHERE a.appUser.id = ?1")
    List<AppUsersQuestionFormsDTO> findAllQuestionFormsFilledOutByUser(long appUserId);

    @Query("SELECT a from AnswerForm  a where a.id = ?1")
    Optional<AnswerForm> findById(long answerFormId);

    @Query("SELECT q.id from AnswerForm a JOIN a.questionForm q where a.appUser.id = ?1")
    List<Long> findAllQuestionFormIdsFilledOutByAppUser(long appUserId);

}
