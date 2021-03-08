package com.bottomupquestionphd.demo.domains.dtos.appuser;

public interface AppUsersQuestionFormsDTO {
//using projection https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#projections
    long getQuestionFormId();

    String getName();

    long getAnswerFormId();

    long getAppUserId();

}
