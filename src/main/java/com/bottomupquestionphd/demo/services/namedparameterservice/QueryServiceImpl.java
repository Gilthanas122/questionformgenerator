package com.bottomupquestionphd.demo.services.namedparameterservice;

import com.bottomupquestionphd.demo.domains.daos.answers.ActualAnswerText;
import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormNotFilledOutByUserDTO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class QueryServiceImpl implements QueryService {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @PersistenceContext
    private final EntityManager em;

    public QueryServiceImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, EntityManager em) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.em = em;
    }

    @Override
    public List<QuestionFormNotFilledOutByUserDTO> findAllQuestionFormNotFilledOutByUser(List<Long> ids) {
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        if (ids == null || ids.isEmpty()) {
            return namedParameterJdbcTemplate.query(
                    "SELECT qf.id, qf.name, COUNT(q.question_form_id) as numberofquestions FROM questions q JOIN questionforms qf ON q.question_form_id = qf.id  " +
                            " GROUP BY qf.id", parameters,
                    (rs, rownum) -> new QuestionFormNotFilledOutByUserDTO(rs.getLong("id"), rs.getString("name"), rs.getInt("numberofquestions"))
            );
        }

        return namedParameterJdbcTemplate.query(
                "SELECT qf.id, qf.name, COUNT(q.question_form_id) as numberofquestions FROM questions q JOIN questionforms qf ON q.question_form_id = qf.id  " +
                        "WHERE qf.id NOT IN (:ids) GROUP BY qf.id", parameters,
                (rs, rownum) -> new QuestionFormNotFilledOutByUserDTO(rs.getLong("id"), rs.getString("name"), rs.getInt("numberofquestions"))
        );
    }

    @Override
    @Modifying
    @Transactional
    public void deleteQuestionsBelongingToQuestionForm(long questionFormId) {
        em.createQuery("UPDATE Question q SET q.deleted = 1 where q.questionForm.id = ?1")
                .setParameter(1, questionFormId)
                .executeUpdate();
    }

    @Override
    public List<ActualAnswerText> findAllActualAnswersBelongingToQuestions(List<Long> questionIds) {
        SqlParameterSource parameters = new MapSqlParameterSource("ids", questionIds);
        if (questionIds == null || questionIds.size() < 1) {
            return new ArrayList<ActualAnswerText>();
        }
        return namedParameterJdbcTemplate.query(
                "SELECT aat.id as id, aat.answer_text as answer_text FROM actualanswertexts aat JOIN answers a ON aat.answer_id =  a.id WHERE a.question_id IN (:ids)", parameters,
                (rs, rownum) -> new ActualAnswerText(rs.getLong("id"), rs.getString("answer_text")));
    }
}
