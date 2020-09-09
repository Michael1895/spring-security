package com.example.springsecurity.payload.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class MovesDTO {

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty
    private LocalDateTime created;

    private String id_map;

    private Boolean is_valid_move;

    private List<String> answers;

    private Double location_lat;

    private Double location_long;

    private UUID resource_id;

    private UUID game_id;

    private UUID scene_id;

    private UUID annotation_answer_id;

    private Integer points;

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getId_map() {
        return id_map;
    }

    public void setId_map(String id_map) {
        this.id_map = id_map;
    }

    public Boolean getIs_valid_move() {
        return is_valid_move;
    }

    public void setIs_valid_move(Boolean is_valid_move) {
        this.is_valid_move = is_valid_move;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public UUID getResource_id() {
        return resource_id;
    }

    public void setResource_id(UUID resource_id) {
        this.resource_id = resource_id;
    }

    public UUID getGame_id() {
        return game_id;
    }

    public void setGame_id(UUID game_id) {
        this.game_id = game_id;
    }

    public UUID getScene_id() {
        return scene_id;
    }

    public void setScene_id(UUID scene_id) {
        this.scene_id = scene_id;
    }

    public UUID getAnnotation_answer_id() {
        return annotation_answer_id;
    }

    public void setAnnotation_answer_id(UUID annotation_answer_id) {
        this.annotation_answer_id = annotation_answer_id;
    }

    public Double getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(Double location_lat) {
        this.location_lat = location_lat;
    }

    public Double getLocation_long() {
        return location_long;
    }

    public void setLocation_long(Double location_long) {
        this.location_long = location_long;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
