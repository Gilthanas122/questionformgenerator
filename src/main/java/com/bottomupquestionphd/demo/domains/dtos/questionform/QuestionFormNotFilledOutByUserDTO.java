package com.bottomupquestionphd.demo.domains.dtos.questionform;

public class QuestionFormNotFilledOutByUserDTO {
    private long questionFormId;
    private String name;

    public QuestionFormNotFilledOutByUserDTO() {
    }

    public QuestionFormNotFilledOutByUserDTO(long questionFormId, String name) {
        this.questionFormId = questionFormId;
        this.name = name;
    }

    public long getQuestionFormId() {
        return questionFormId;
    }

    public void setQuestionFormId(long questionFormId) {
        this.questionFormId = questionFormId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
