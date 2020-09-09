package com.example.springsecurity.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
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
@Table(name = "annotation_answers")
@TypeDef(
        name = "jsonb",
        typeClass = JsonBinaryType.class
)
public class AnnotationAnswers {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid", strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    private LocalDateTime created;

    private LocalDateTime updated;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String answer_values;

    @JsonBackReference(value="annotations-annotation_answers")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="annotation_id", referencedColumnName="id")
    private Annotations annotations;

    @JsonBackReference(value="users-annotation_answers")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;

    public AnnotationAnswers() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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
