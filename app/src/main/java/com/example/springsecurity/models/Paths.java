package com.example.springsecurity.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "paths")
public class Paths {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid", strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "uuid" )
    private UUID id;

    private String finish_message;

    private String name;

    private String story_back_cover_colour;

    private String story_back_cover_text_colour;

    @Column(columnDefinition="TEXT")
    private String story_finish_message;

    @JsonManagedReference(value="paths-resources1")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private Resources image_id;

    @JsonManagedReference(value="paths-resources2")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "story_image_id", referencedColumnName = "id")
    private Resources story_image_id;

    //@JsonBackReference(value="scenes-paths")
    @ManyToMany(mappedBy = "paths")
    private Set<Scenes> scenes  = new HashSet<>();


    @JsonBackReference(value="games-paths")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id", referencedColumnName="id")
    private Games game_id;

    public Paths() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    @JsonIgnore
    public Resources getImage_id() {
        return image_id;
    }

    public void setImage_id(Resources image_id) {
        this.image_id = image_id;
    }

    @JsonIgnore
    public Resources getStory_image_id() {
        return story_image_id;
    }

    public void setStory_image_id(Resources story_image_id) {
        this.story_image_id = story_image_id;
    }

    /*public Set<Scenes> getScenes() {
        return scenes;
    }

    public void setScenes(Set<Scenes> scenes) {
        this.scenes = scenes;
    }*/
    @JsonIgnore
    public Games getGames() {
        return game_id;
    }

    public void setGames(Games game_id) {
        this.game_id = game_id;
    }

    public Set<Scenes> getScenes() {
        return scenes;
    }

    public void setScenes(Set<Scenes> scenes) {
        this.scenes = scenes;
    }
}
