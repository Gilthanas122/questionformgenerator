package com.bottomupquestionphd.demo.services.appuser;

import com.bottomupquestionphd.demo.domains.dtos.appuser.AppUsersQuestionFormsDTO;
import com.bottomupquestionphd.demo.domains.dtos.questionform.QuestionFormNotFilledOutByUserDTO;
import com.bottomupquestionphd.demo.exceptions.appuser.BelongToAnotherUserException;
import com.bottomupquestionphd.demo.services.answerforms.AnswerFormService;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppUserContentServiceImpl implements AppUserContentService {
    private final AnswerFormService answerFormService;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final AppUserService appUserService;

    public AppUserContentServiceImpl(AnswerFormService answerFormService, NamedParameterJdbcTemplate namedParameterJdbcTemplate, AppUserService appUserService) {
        this.answerFormService = answerFormService;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.appUserService = appUserService;
    }

    @Override
    public List<AppUsersQuestionFormsDTO> findQuestionFormsFilledOutByUser(long appUserId) throws BelongToAnotherUserException {
        return answerFormService.findQuestionFormsFilledOutByAppUserId(appUserId);
    }

    @Override
    public List<QuestionFormNotFilledOutByUserDTO> findAllQuestionFormsNotFilledOutByUser(long appUserId) throws BelongToAnotherUserException {
        appUserService.checkIfCurrentUserMatchesUserIdInPath(appUserId);
        List<Long> ids = answerFormService.findQuestionFormIdsFilledOutByUser(appUserId);
        SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
        if (ids == null || ids.isEmpty()){
            return namedParameterJdbcTemplate.query(
                    "SELECT id, name FROM questionforms ", parameters,
                    (rs, rownum) -> new QuestionFormNotFilledOutByUserDTO(rs.getLong("id"), rs.getString("name"))
            );
        }


        return namedParameterJdbcTemplate.query(
                "SELECT id, name FROM questionforms where id NOT IN (:ids)", parameters,
                (rs, rownum) -> new QuestionFormNotFilledOutByUserDTO(rs.getLong("id"), rs.getString("name"))
        );
    }

    @Override
    public long findCurrentlyLoggedInUsersId() {
        return appUserService.findCurrentlyLoggedInUser().getId();
    }
}
