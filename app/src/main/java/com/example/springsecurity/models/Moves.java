package com.example.springsecurity.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "moves")
@TypeDef(
        name = "list-array",
        typeClass = ListArrayType.class
)
public class Moves {
    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid", strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "uuid" )
    private UUID id;

    private LocalDateTime created;

    private String id_map;

    private Boolean is_valid_move;

    @Type(type = "list-array")
    @Column(
            name = "answers",
            columnDefinition = "text[]"
    )
    private List<String> answers;

    private Double location_lat;

    private Double location_long;

    @JsonBackReference(value="user-moves")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id", referencedColumnName="id")
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(	name = "moves_resources_join",
            joinColumns = @JoinColumn(name = "move_id"),
            inverseJoinColumns = @JoinColumn(name = "resource_id"))
    private Set<Resources> resources = new HashSet<>();

    private UUID game_id;

    private UUID scene_id;

    private UUID annotation_answer_id;

    private Integer points;

    public Moves() {
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

    public String getId_map() {
        return id_map;
    }

    public void setId_map(String id_map) {
        this.id_map = id_map;
    }

    public Set<Resources> getResources() {
        return resources;
    }

    public void setResources(Set<Resources> resources) {
        this.resources = resources;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
