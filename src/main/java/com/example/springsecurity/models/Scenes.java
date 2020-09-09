package com.example.springsecurity.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


import org.hibernate.annotations.*;
//import org.postgis.Point;
//import org.postgis.Geometry;
//import org.springframework.data.geo.Point;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.hibernate.annotations.FetchMode.SELECT;

@Entity
@Table(name = "scenes")
public class Scenes {

    //public enum RadiusUnit {m, km, mi, ft};

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid", strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(columnDefinition="TEXT")
    private String help_message;

    private String id_mapping;

    @Column(columnDefinition = "boolean default false")
    private Boolean image_upload = false;

    @Column(columnDefinition="TEXT")
    private String instruction;

    @Column(columnDefinition = "boolean default true")
    private Boolean is_skippable = true;

    private Integer scene_position;

    @Column(columnDefinition = "boolean default false")
    private Boolean sound_upload = false;

    @Column(columnDefinition="TEXT")
    private String story_extra_help;

    @Column(columnDefinition="TEXT")
    private String story_instruction;

    @Column(columnDefinition="TEXT")
    private String story_page_background;

    @Column(columnDefinition="TEXT")
    private String story_success_message;

    private String story_text_colour;

    @Column(columnDefinition="TEXT")
    private String success_message;

    @Column(columnDefinition = "boolean default false")
    private Boolean video_upload = false;

    private String title;

    @JsonManagedReference(value="scenes-annotations")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "annotation_id", referencedColumnName = "id")
    private Annotations annotation_id;

    @JsonBackReference(value="scenes-games")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id", referencedColumnName="id")
    private Games games;

    @JsonManagedReference(value="scenes-questions")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Questions question_id;

    @JsonManagedReference(value="scenes-scenePoints")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "scene_points_id", referencedColumnName = "id")
    private ScenePoints scene_points_id;

    @Enumerated(EnumType.STRING)
    //@Column(columnDefinition = "varchar(255) default 'm' ")
    private RadiusUnit scene_location_radius_unit = RadiusUnit.m;

    private Double scene_location_radius;

    private Double scene_location_lat;

    private Double scene_location_long;

    @JsonManagedReference(value="scenes-resources1")
    @OneToOne
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Resources image_id;

    @JsonManagedReference(value="scenes-resources2")
    @OneToOne
    @JoinColumn(name = "story_image_id", referencedColumnName = "id")
    private Resources story_image_id;


    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(	name = "paths_scenes_join",
            joinColumns = @JoinColumn(name = "scene_id"),
            inverseJoinColumns = @JoinColumn(name = "path_id"))
    private Set<Paths> paths = new HashSet<>();

    public Scenes() {
    }

    public Scenes(String title, String instruction) {
        this.title = title;
        this.instruction = instruction;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHelp_message() {
        return help_message;
    }

    public void setHelp_message(String help_message) {
        this.help_message = help_message;
    }

    public String getId_mapping() {
        return id_mapping;
    }

    public void setId_mapping(String id_mapping) {
        this.id_mapping = id_mapping;
    }

    public Boolean getImage_upload() {
        return image_upload;
    }

    public void setImage_upload(Boolean image_upload) {
        this.image_upload = image_upload;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Boolean getIs_skippable() {
        return is_skippable;
    }

    public void setIs_skippable(Boolean is_skippable) {
        this.is_skippable = is_skippable;
    }

    public Integer getScene_position() {
        return scene_position;
    }

    public void setScene_position(Integer scene_position) {
        this.scene_position = scene_position;
    }

    public Boolean getSound_upload() {
        return sound_upload;
    }

    public void setSound_upload(Boolean sound_upload) {
        this.sound_upload = sound_upload;
    }

    public String getStory_extra_help() {
        return story_extra_help;
    }

    public void setStory_extra_help(String story_extra_help) {
        this.story_extra_help = story_extra_help;
    }

    public String getStory_instruction() {
        return story_instruction;
    }

    public void setStory_instruction(String story_instruction) {
        this.story_instruction = story_instruction;
    }

    public String getStory_page_background() {
        return story_page_background;
    }

    public void setStory_page_background(String story_page_background) {
        this.story_page_background = story_page_background;
    }

    public String getStory_success_message() {
        return story_success_message;
    }

    public void setStory_success_message(String story_success_message) {
        this.story_success_message = story_success_message;
    }

    public String getStory_text_colour() {
        return story_text_colour;
    }

    public void setStory_text_colour(String story_text_colour) {
        this.story_text_colour = story_text_colour;
    }

    public String getSuccess_message() {
        return success_message;
    }

    public void setSuccess_message(String success_message) {
        this.success_message = success_message;
    }

    public Boolean getVideo_upload() {
        return video_upload;
    }

    public void setVideo_upload(Boolean video_upload) {
        this.video_upload = video_upload;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Annotations getAnnotation_id() {
        return annotation_id;
    }

    public void setAnnotation_id(Annotations annotation_id) {
        this.annotation_id = annotation_id;
    }

    public Games getGames() {
        return games;
    }

    public void setGames(Games games) {
        this.games = games;
    }

    public Questions getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Questions question_id) {
        this.question_id = question_id;
    }

    public ScenePoints getScene_points_id() {
        return scene_points_id;
    }

    public void setScene_points_id(ScenePoints scene_points_id) {
        this.scene_points_id = scene_points_id;
    }

    public RadiusUnit getScene_location_radius_unit() {
        return scene_location_radius_unit;
    }

    public void setScene_location_radius_unit(RadiusUnit scene_location_radius_unit) {
        this.scene_location_radius_unit = scene_location_radius_unit;
    }

    public Resources getImage_id() {
        return image_id;
    }

    public void setImage_id(Resources image_id) {
        this.image_id = image_id;
    }

    public Resources getStory_image_id() {
        return story_image_id;
    }

    public void setStory_image_id(Resources story_image_id) {
        this.story_image_id = story_image_id;
    }

    public Set<Paths> getPaths() {
        return paths;
    }

    public void setPaths(Set<Paths> paths) {
        this.paths = paths;
    }

    public Double getScene_location_radius() {
        return scene_location_radius;
    }

    public void setScene_location_radius(Double scene_location_radius) {
        this.scene_location_radius = scene_location_radius;
    }

    public Double getScene_location_lat() {
        return scene_location_lat;
    }

    public void setScene_location_lat(Double scene_location_lat) {
        this.scene_location_lat = scene_location_lat;
    }

    public Double getScene_location_long() {
        return scene_location_long;
    }

    public void setScene_location_long(Double scene_location_long) {
        this.scene_location_long = scene_location_long;
    }
}
