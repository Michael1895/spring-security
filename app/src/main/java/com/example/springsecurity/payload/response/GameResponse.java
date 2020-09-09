package com.example.springsecurity.payload.response;

import com.example.springsecurity.models.Paths;
import com.example.springsecurity.models.Resources;
import com.example.springsecurity.models.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class GameResponse {
    private Boolean isRegistered;

    private String title;

    private Boolean create_story;

    private String description;

    /*@JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)*/
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty
    private LocalDateTime ends;

    private Boolean is_public;

    private Integer max_skips;

    private String game_mode;

    private Integer participants_limit;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty
    private LocalDateTime registrations_end;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty
    private LocalDateTime registrations_start;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @JsonProperty
    private LocalDateTime starts;

    private String story_cover_colour;

    private String story_cover_text_colour;

    private String story_title;

    private String terms;

    private Set<Paths> paths;

    private Resources image_id;

    private UUID user_id;

    private Resources story_image_id;

    public GameResponse(Boolean isRegistered, String title, Boolean create_story, String description, LocalDateTime ends, Boolean is_public, Integer max_skips, String game_mode, Integer participants_limit, LocalDateTime registrations_end, LocalDateTime registrations_start, LocalDateTime starts, String story_cover_colour, String story_cover_text_colour, String story_title, String terms, Set<Paths> paths, Resources image_id, UUID user_id, Resources story_image_id) {

        this.isRegistered = isRegistered;
        this.title = title;
        this.create_story = create_story;
        this.description = description;
        this.ends = ends;
        this.is_public = is_public;
        this.max_skips = max_skips;
        this.game_mode = game_mode;
        this.participants_limit = participants_limit;
        this.registrations_end = registrations_end;
        this.registrations_start = registrations_start;
        this.starts = starts;
        this.story_cover_colour = story_cover_colour;
        this.story_cover_text_colour = story_cover_text_colour;
        this.story_title = story_title;
        this.terms = terms;
        this.paths = paths;
        this.image_id = image_id;
        this.user_id = user_id;
        this.story_image_id = story_image_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCreate_story() {
        return create_story;
    }

    public void setCreate_story(Boolean create_story) {
        this.create_story = create_story;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getEnds() {
        return ends;
    }

    public void setEnds(LocalDateTime ends) {
        this.ends = ends;
    }

    public Boolean getIs_public() {
        return is_public;
    }

    public void setIs_public(Boolean is_public) {
        this.is_public = is_public;
    }

    public Integer getMax_skips() {
        return max_skips;
    }

    public void setMax_skips(Integer max_skips) {
        this.max_skips = max_skips;
    }

    public String getGame_mode() {
        return game_mode;
    }

    public void setGame_mode(String game_mode) {
        this.game_mode = game_mode;
    }

    public Integer getParticipants_limit() {
        return participants_limit;
    }

    public void setParticipants_limit(Integer participants_limit) {
        this.participants_limit = participants_limit;
    }

    public LocalDateTime getRegistrations_ends() {
        return registrations_end;
    }

    public void setRegistrations_ends(LocalDateTime registrations_end) {
        this.registrations_end = registrations_end;
    }

    public LocalDateTime getRegistrations_start() {
        return registrations_start;
    }

    public void setRegistrations_start(LocalDateTime registrations_start) {
        this.registrations_start = registrations_start;
    }

    public LocalDateTime getStarts() {
        return starts;
    }

    public void setStarts(LocalDateTime starts) {
        this.starts = starts;
    }

    public String getStory_cover_colour() {
        return story_cover_colour;
    }

    public void setStory_cover_colour(String story_cover_colour) {
        this.story_cover_colour = story_cover_colour;
    }

    public String getStory_cover_text_colour() {
        return story_cover_text_colour;
    }

    public void setStory_cover_text_colour(String story_cover_text_colour) {
        this.story_cover_text_colour = story_cover_text_colour;
    }

    public String getStory_title() {
        return story_title;
    }

    public void setStory_title(String story_title) {
        this.story_title = story_title;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public Set<Paths> getPaths() {
        return paths;
    }

    public void setPaths(Set<Paths> paths) {
        this.paths = paths;
    }

    public Resources getImage_id() {
        return image_id;
    }

    public void setImage_id(Resources image_id) {
        this.image_id = image_id;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public void setUser_id(UUID user_id) {
        this.user_id = user_id;
    }

    public Resources getStory_image_id() {
        return story_image_id;
    }

    public void setStory_image_id(Resources story_image_id) {
        this.story_image_id = story_image_id;
    }

    public Boolean getRegistered() {
        return isRegistered;
    }

    public void setRegistered(Boolean registered) {
        isRegistered = registered;
    }
}
