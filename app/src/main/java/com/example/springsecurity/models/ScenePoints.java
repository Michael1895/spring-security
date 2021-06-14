package com.example.springsecurity.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "scene_points")
public class ScenePoints {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid", strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "uuid" )
    private UUID id;

    private Integer maximum_points;

    private Integer minimum_points;

    private Integer penalty_points;

    private Integer remove_interval;

    @Column(columnDefinition = "boolean default true")
    private Boolean remove_on_wrong_answer;

    private Boolean time_based;

    private Boolean remove_on_hint;

    //@JsonIgnore
    @JsonBackReference(value="scenes-scenePoints")
    @OneToOne(mappedBy = "scene_points_id", fetch = FetchType.EAGER)
    private Scenes scenes;

    public ScenePoints() {
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getMaximum_points() {
        return maximum_points;
    }

    public void setMaximum_points(Integer maximum_points) {
        this.maximum_points = maximum_points;
    }

    public Integer getMinimum_points() {
        return minimum_points;
    }

    public void setMinimum_points(Integer minimum_points) {
        this.minimum_points = minimum_points;
    }

    public Integer getPenalty_points() {
        return penalty_points;
    }

    public void setPenalty_points(Integer penalty_points) {
        this.penalty_points = penalty_points;
    }

    public Integer getRemove_interval() {
        return remove_interval;
    }

    public void setRemove_interval(Integer remove_interval) {
        this.remove_interval = remove_interval;
    }

    public Boolean getTime_based() {
        return time_based;
    }

    public void setTime_based(Boolean time_based) {
        this.time_based = time_based;
    }

    public Boolean getRemove_on_hint() {
        return remove_on_hint;
    }

    public void setRemove_on_hint(Boolean remove_on_hint) {
        this.remove_on_hint = remove_on_hint;
    }

    public Scenes getScenes() {
        return scenes;
    }

    public void setScenes(Scenes scenes) {
        this.scenes = scenes;
    }

    public Boolean getRemove_on_wrong_answer() {
        return remove_on_wrong_answer;
    }

    public void setRemove_on_wrong_answer(Boolean remove_on_wrong_answer) {
        this.remove_on_wrong_answer = remove_on_wrong_answer;
    }
}
