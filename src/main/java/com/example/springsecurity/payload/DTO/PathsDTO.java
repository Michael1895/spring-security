package com.example.springsecurity.payload.DTO;

import com.example.springsecurity.models.Resources;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.util.UUID;

public class PathsDTO {

    private UUID scene_id;

    private String finish_message;

    private String name;

    private String story_back_cover_colour;

    private String story_back_cover_text_colour;

    private String story_finish_message;

    private UUID image_id;

    private UUID story_image_id;

    public UUID getScene_id() {
        return scene_id;
    }

    public void setScene_id(UUID scene_id) {
        this.scene_id = scene_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFinish_message() {
        return finish_message;
    }

    public void setFinish_message(String finish_message) {
        this.finish_message = finish_message;
    }

    public String getStory_back_cover_colour() {
        return story_back_cover_colour;
    }

    public void setStory_back_cover_colour(String story_back_cover_colour) {
        this.story_back_cover_colour = story_back_cover_colour;
    }

    public String getStory_back_cover_text_colour() {
        return story_back_cover_text_colour;
    }

    public void setStory_back_cover_text_colour(String story_back_cover_text_colour) {
        this.story_back_cover_text_colour = story_back_cover_text_colour;
    }

    public String getStory_finish_message() {
        return story_finish_message;
    }

    public void setStory_finish_message(String story_finish_message) {
        this.story_finish_message = story_finish_message;
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
}
