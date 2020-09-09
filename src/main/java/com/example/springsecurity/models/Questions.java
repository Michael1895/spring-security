package com.example.springsecurity.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "questions")
public class Questions {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid", strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private AcceptsType accepts;

    @Column(columnDefinition = "boolean default false")
    private Boolean case_sensitive_text_answers;

    @Column(columnDefinition="TEXT")
    private String message;

    @JsonManagedReference(value="questions-answers")
    @OneToMany(targetEntity=Answers.class, fetch = FetchType.EAGER, mappedBy="questions", cascade = CascadeType.REMOVE)
    private Set<Answers> answers = new HashSet<>();

    @JsonBackReference(value="scenes-questions")
    @OneToOne(mappedBy = "question_id", fetch = FetchType.EAGER)
    private Scenes scenes;

    public Questions() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public Scenes getScenes() {
        return scenes;
    }

    public void setScenes(Scenes scenes) {
        this.scenes = scenes;
    }
}
