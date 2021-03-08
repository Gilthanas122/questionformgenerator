package com.bottomupquestionphd.demo.domains.dtos.appuser;

public class AppUsersQuestionFormsDTO {
    private long questionFormId;
    private String questionFormName;

    public AppUsersQuestionFormsDTO() {
    }

    public AppUsersQuestionFormsDTO(long questionFormId, String questionFormName) {
        this.questionFormId = questionFormId;
        this.questionFormName = questionFormName;
    }

    public long getQuestionFormId() {
        return questionFormId;
    }

    public void setQuestionFormId(long questionFormId) {
        this.questionFormId = questionFormId;
    }

    public String getQuestionFormName() {
        return questionFormName;
    }

    public void setQuestionFormName(String questionFormName) {
        this.questionFormName = questionFormName;
    }
}
