package com.bottomupquestionphd.demo.domains.daos.answers;

import com.bottomupquestionphd.demo.domains.daos.questions.Question;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "answers")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany(mappedBy = "answer", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<ActualAnswerText> actualAnswerTexts = new ArrayList<>();

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private AnswerForm answerForm;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Question question;

    public Answer() {
    }

    public Answer(List<ActualAnswerText> actualAnswerTexts) {
        this.actualAnswerTexts = actualAnswerTexts;
    }

    public Answer(ActualAnswerText actualAnswerText) {
        this.actualAnswerTexts.add(actualAnswerText);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<ActualAnswerText> getActualAnswerTexts() {
        return actualAnswerTexts;
    }

    public void setActualAnswerTexts(List<ActualAnswerText> actualAnswerTexts) {
        this.actualAnswerTexts = actualAnswerTexts;
    }

    public AnswerForm getAnswerForm() {
        return answerForm;
    }

    public void setAnswerForm(AnswerForm answerForm) {
        this.answerForm = answerForm;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
