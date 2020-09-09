package com.example.springsecurity.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "games")
public class Games {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid", strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    @Type(type="pg-uuid")
    private UUID id;

    @NotBlank
    private String title;

    @Column(columnDefinition = "boolean default false")
    private Boolean create_story = false;

    @Column(columnDefinition="TEXT")
    private String description;

    private LocalDateTime ends;

    @Column(columnDefinition = "boolean default true")
    private Boolean is_public = true;

    private Integer max_skips;

    @Column(columnDefinition = "varchar(255) default 'path'")
    private String game_mode;

    private Integer participants_limit;

    private LocalDateTime registrations_end;

    private LocalDateTime registrations_start;

    private LocalDateTime starts;

    @Column(columnDefinition = "varchar(255) default '#FFF'")
    private String story_cover_colour;

    @Column(columnDefinition = "varchar(255) default '#000'")
    private String story_cover_text_colour;

    @Column
    private String story_title;

    @Column(columnDefinition="TEXT")
    private String terms;


    @JsonManagedReference(value="games-resources1")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Resources image_id;


    @JsonManagedReference(value="games-resources2")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "story_image_id", referencedColumnName = "id")
    private Resources story_image_id;

    @JsonBackReference(value="user-games")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", referencedColumnName="id")
    private User user;

    @JsonManagedReference(value="scenes-games")
    @OneToMany(targetEntity=Scenes.class, fetch = FetchType.EAGER, mappedBy="games", cascade = CascadeType.REMOVE)
    private Set<Scenes> scenes = new HashSet<>();


    @JsonIgnore
    @JsonManagedReference(value="games-paths")
    @OneToMany(targetEntity=Paths.class, fetch = FetchType.EAGER, mappedBy="game_id", cascade = CascadeType.REMOVE)
    private Set<Paths> paths = new HashSet<>();

    @JsonManagedReference(value="game-game_registrations")
    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
    private Set<GameRegistrations> registrations = new HashSet<>();

    public Games() {
    }

    public Games(String title, String description, Resources image_id) {
        this.title = title;
        this.description = description;
        this.image_id = image_id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Resources getStory_image_id() {
        return story_image_id;
    }

    public void setStory_image_id(Resources story_image_id) {
        this.story_image_id = story_image_id;
    }


    public Set<Scenes> getScenes() {
        return scenes;
    }

    public void setScenes(Set<Scenes> scenes) {
        this.scenes = scenes;
    }

    public Set<GameRegistrations> getRegistrations() {
        return registrations;
    }

    public void setRegistrations(Set<GameRegistrations> registrations) {
        this.registrations = registrations;
    }
}

