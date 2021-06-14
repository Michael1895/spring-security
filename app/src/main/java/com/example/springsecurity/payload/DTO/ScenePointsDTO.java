package com.example.springsecurity.payload.DTO;

import com.example.springsecurity.models.Scenes;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.util.UUID;

public class ScenePointsDTO {

    private Integer maximum_points;

    private Integer minimum_points;

    private Integer penalty_points;

    private Integer remove_interval;

    private Boolean remove_on_wrong_answer;

    private Boolean time_based;

    private Boolean remove_on_hint;

    private Scenes scenes;

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
