package com.example.springsecurity.payload.DTO;

import com.example.springsecurity.models.Annotations;
import com.example.springsecurity.models.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.UUID;

public class AnnotationAnswersDTO {

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty
    private LocalDateTime created;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty
    private LocalDateTime updated;

    private String answer_values; //json einai auto

    private Annotations annotations;

    private User user;

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public String getAnswer_values() {
        return answer_values;
    }

    public void setAnswer_values(String answer_values) {
        this.answer_values = answer_values;
    }

    public Annotations getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Annotations annotations) {
        this.annotations = annotations;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
