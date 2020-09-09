package com.example.springsecurity.payload.DTO;

import com.example.springsecurity.models.*;
//import org.geolatte.geom.Point;
//import org.springframework.data.geo.Point;
//import org.postgis.Point;
import com.vividsolutions.jts.geom.Point;
//import org.postgis.Geometry;
//import com.vividsolutions.jts.geom.Geometry;


import java.util.Set;
import java.util.UUID;

public class ScenesDTO {


    private String description;

    private String help_message;

    private String id_mapping;

    private Boolean image_upload;

    private String instruction;

    private Boolean is_skippable;

    private Integer scene_position;

    private Boolean sound_upload;

    private String story_extra_help;

    private String story_instruction;

    private String story_page_background;

    private String story_success_message;

    private String story_text_colour;

    private String success_message;

    private Boolean video_upload;

    private String title;

    private UUID annotation_id;

    private UUID game_id;

    private UUID question_id;

    private UUID scene_points_id;

    private RadiusUnit scene_location_radius_unit;

    private Double scene_location_radius;

    private Double scene_location_lat;

    private Double scene_location_long;

    private UUID image_id;

    private UUID story_image_id;

    private Set<Paths> paths;


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

    public UUID getAnnotation_id() {
        return annotation_id;
    }

    public void setAnnotation_id(UUID annotation_id) {
        this.annotation_id = annotation_id;
    }

    public UUID getGame_id() {
        return game_id;
    }

    public void setGame_id(UUID game_id) {
        this.game_id = game_id;
    }

    public UUID getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(UUID question_id) {
        this.question_id = question_id;
    }

    public UUID getScene_points_id() {
        return scene_points_id;
    }

    public void setScene_points_id(UUID scene_points_id) {
        this.scene_points_id = scene_points_id;
    }

    public RadiusUnit getScene_location_radius_unit() {
        return scene_location_radius_unit;
    }

    public void setScene_location_radius_unit(RadiusUnit scene_location_radius_unit) {
        this.scene_location_radius_unit = scene_location_radius_unit;
    }

    public Double getScene_location_radius() {
        return scene_location_radius;
    }

    public void setScene_location_radius(Double scene_location_radius) {
        this.scene_location_radius = scene_location_radius;
    }

    public UUID getImage_id() {
        return image_id;
    }

    public void setImage_id(UUID image_id) {
        this.image_id = image_id;
    }

    public UUID getStory_image_id() {
        return story_image_id;
    }

    public void setStory_image_id(UUID story_image_id) {
        this.story_image_id = story_image_id;
    }

    public Set<Paths> getPaths() {
        return paths;
    }

    public void setPaths(Set<Paths> paths) {
        this.paths = paths;
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