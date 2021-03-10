package com.bottomupquestionphd.demo.services.namedparameterservice;

import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormNotFilledOutByUserDTO;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
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
    public List<QuestionFormNotFilledOutByUserDTO> findAllQuestionFormNotFilledOutByUser(List<Long> ids){
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        if (ids == null || ids.isEmpty()){
            return namedParameterJdbcTemplate.query(
                    "SELECT id, name FROM questionforms WHERE deleted = 0", parameters,
                    (rs, rownum) -> new QuestionFormNotFilledOutByUserDTO(rs.getLong("id"), rs.getString("name"))
            );
        }

        return namedParameterJdbcTemplate.query(
                "SELECT id, name FROM questionforms where id NOT IN (:ids) AND deleted=0", parameters,
                (rs, rownum) -> new QuestionFormNotFilledOutByUserDTO(rs.getLong("id"), rs.getString("name"))
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
}
