package com.example.springsecurity.payload.DTO;

import com.example.springsecurity.models.AcceptsType;
import com.example.springsecurity.models.Answers;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class QuestionsDTO {

    private AcceptsType accepts;

    private Boolean case_sensitive_text_answers;

    private String message;

    private Set<Answers> answers = new HashSet<>();

    private UUID scenes;

    public AcceptsType getAccepts() {
        return accepts;
    }

    public void setAccepts(AcceptsType accepts) {
        this.accepts = accepts;
    }

    public Boolean getCase_sensitive_text_answers() {
        return case_sensitive_text_answers;
    }

    public void setCase_sensitive_text_answers(Boolean case_sensitive_text_answers) {
        this.case_sensitive_text_answers = case_sensitive_text_answers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<Answers> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<Answers> answers) {
        this.answers = answers;
    }

    public UUID getScenes() {
        return scenes;
    }

    public void setScenes(UUID scenes) {
        this.scenes = scenes;
    }

}
