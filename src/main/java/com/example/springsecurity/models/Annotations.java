package com.example.springsecurity.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "annotations")
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
public class Annotations {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid", strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    @Type(type = "list-array")
    @Column(
            name = "answer_labels",
            columnDefinition = "text[]"
    )
    private List<String> answer_labels;

    private String image;

    private String instruction;

    @Column(columnDefinition = "boolean default false")
    private Boolean is_public;

    @Column(columnDefinition = "boolean default false")
    private Boolean order_resources_by_usage;

    private Integer split_resources_by;

    private String title;

    @Enumerated(EnumType.STRING)
    private AnnotationType annotation_type;

    @JsonBackReference(value="scenes-annotations")
    @OneToOne(mappedBy = "annotation_id", fetch = FetchType.EAGER)
    private Scenes scene;

    private UUID scene_id;

    @JsonManagedReference(value="annotations-annotation_answers")
    @OneToMany(targetEntity=AnnotationAnswers.class, fetch = FetchType.EAGER, mappedBy="user", cascade = CascadeType.REMOVE)
    private Set<AnnotationAnswers> annotation_answers = new HashSet<>(); //na to epistrefei?

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "resources_annotations_join",
            joinColumns = @JoinColumn(name = "annotation_id"),
            inverseJoinColumns = @JoinColumn(name = "resource_id"))
    private Set<Resources> resources = new HashSet<>(); // mporei na mi xreiazetai avatar id sth bash

    public Annotations() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<String> getAnswer_labels() {
        return answer_labels;
    }

    public void setAnswer_labels(List<String> answer_labels) {
        this.answer_labels = answer_labels;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Boolean getIs_public() {
        return is_public;
    }

    public void setIs_public(Boolean is_public) {
        this.is_public = is_public;
    }

    public Boolean getOrder_resources_by_usage() {
        return order_resources_by_usage;
    }

    public void setOrder_resources_by_usage(Boolean order_resources_by_usage) {
        this.order_resources_by_usage = order_resources_by_usage;
    }

    public Integer getSplit_resources_by() {
        return split_resources_by;
    }

    public void setSplit_resources_by(Integer split_resources_by) {
        this.split_resources_by = split_resources_by;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AnnotationType getAnnotation_type() {
        return annotation_type;
    }

    public void setAnnotation_type(AnnotationType annotation_type) {
        this.annotation_type = annotation_type;
    }

    public Scenes getScene() {
        return scene;
    }

    public void setScene(Scenes scene) {
        this.scene = scene;
    }

    public Set<Resources> getResources() {
        return resources;
    }

    public void setResources(Set<Resources> resources) {
        this.resources = resources;
    }

    public Set<AnnotationAnswers> getAnnotation_answers() {
        return annotation_answers;
    }

    public void setAnnotation_answers(Set<AnnotationAnswers> annotation_answers) {
        this.annotation_answers = annotation_answers;
    }

    public UUID getScene_id() {
        return scene_id;
    }

    public void setScene_id(UUID scene_id) {
        this.scene_id = scene_id;
    }
}
