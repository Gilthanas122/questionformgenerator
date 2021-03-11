package com.bottomupquestionphd.demo.domains.dtos.questionform;

public class QuestionFormNotFilledOutByUserDTO {
    private long questionFormId;
    private String name;
    private int numberOfQuestionInQuestionForm;

    public QuestionFormNotFilledOutByUserDTO() {
    }

    public QuestionFormNotFilledOutByUserDTO(long questionFormId, String name, int numberOfQuestionInQuestionForm) {
        this.questionFormId = questionFormId;
        this.name = name;
        this.numberOfQuestionInQuestionForm = numberOfQuestionInQuestionForm;
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

    public int getNumberOfQuestionInQuestionForm() {
        return numberOfQuestionInQuestionForm;
    }

    public void setNumberOfQuestionInQuestionForm(int numberOfQuestionInQuestionForm) {
        this.numberOfQuestionInQuestionForm = numberOfQuestionInQuestionForm;
    }
}
