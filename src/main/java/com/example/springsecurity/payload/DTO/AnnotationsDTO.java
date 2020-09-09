package com.example.springsecurity.payload.DTO;

import com.example.springsecurity.models.AnnotationAnswers;
import com.example.springsecurity.models.AnnotationType;
import com.example.springsecurity.models.Resources;
import com.example.springsecurity.models.Scenes;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AnnotationsDTO {
    private List<String> answer_labels;

    private String image;

    private String instruction;

    private Boolean is_public;

    private Boolean order_resources_by_usage;

    private Integer split_resources_by;

    private String title;

    private AnnotationType annotation_type;

    private UUID scene_id;

    private Set<AnnotationAnswers> annotation_answers = new HashSet<>();

    private Set<Resources> resources = new HashSet<>();

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

    public UUID getScene_id() {
        return scene_id;
    }

    public void setScene_id(UUID scene_id) {
        this.scene_id = scene_id;
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

}
