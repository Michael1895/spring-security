package com.example.springsecurity.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "answers")
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
public class Answers {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid", strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    private String answer_value;

    private String text_value;

    private Double number_value;

    private LocalDateTime date_value;

    @Type(type = "list-array")
    @Column(
            name = "answer_options",
            columnDefinition = "text[]"
    )
    private List<String> answer_options;

    @JsonBackReference(value="questions-answers")
    @ManyToOne
    @JoinColumn(name="question_id", referencedColumnName="id")
    private Questions questions;


    public Answers() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAnswer_value() {
        return answer_value;
    }

    public void setAnswer_value(String answer_value) {
        this.answer_value = answer_value;
    }

    public String getText_value() {
        return text_value;
    }

    public void setText_value(String text_value) {
        this.text_value = text_value;
    }

    public Double getNumber_value() {
        return number_value;
    }

    public void setNumber_value(Double number_value) {
        this.number_value = number_value;
    }

    public LocalDateTime getDate_value() {
        return date_value;
    }

    public void setDate_value(LocalDateTime date_value) {
        this.date_value = date_value;
    }

    public List<String> getAnswer_options() {
        return answer_options;
    }

    public void setAnswer_options(List<String> answer_options) {
        this.answer_options = answer_options;
    }

    public Questions getQuestions() {
        return questions;
    }

    public void setQuestions(Questions questions) {
        this.questions = questions;
    }
}
