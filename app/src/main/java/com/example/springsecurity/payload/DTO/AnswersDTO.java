package com.example.springsecurity.payload.DTO;

import com.example.springsecurity.models.Questions;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AnswersDTO {

    private String answer_value;

    private String text_value;

    private Double number_value;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty
    private LocalDateTime date_value;

    private List<String> answer_options;

    private Questions questions;

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
