package com.bottomupquestionphd.demo.domains.daos.answers;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sun.istack.NotNull;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "textanswervotes")
@Where(clause = "deleted=1")
public class TextAnswerVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private byte value;
    private boolean deleted;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonBackReference(value = "actualAnswerTextsTextAnswerVotes")
    private ActualAnswerText actualAnswerText;

    public TextAnswerVote() {
    }

    public TextAnswerVote(Long id, byte value) {
        this.id = id;
        this.value = value;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public byte getValue() {
        return value;
    }

    public void setValue(byte value) {
        this.value = value;
    }

    public ActualAnswerText getActualAnswerText() {
        return actualAnswerText;
    }

    public void setActualAnswerText(ActualAnswerText actualAnswerText) {
        this.actualAnswerText = actualAnswerText;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
