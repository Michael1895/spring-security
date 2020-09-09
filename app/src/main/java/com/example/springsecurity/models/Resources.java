package com.example.springsecurity.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "resources")

public class Resources  extends AuditModel {

        @Id
        @GenericGenerator(name = "uuid", strategy = "uuid2")
        @GeneratedValue(generator = "uuid", strategy = GenerationType.IDENTITY)
        @Column(name = "id", unique = true, nullable = false, columnDefinition = "uuid" )
        private UUID id;

        private String resource_name;

        private String content_type;

        private long size;

        @JsonBackReference(value="user-resources")
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name="owner_id", referencedColumnName="id")
        private User user;

        @JsonBackReference(value="games-resources1")
        @OneToOne(mappedBy = "image_id", fetch = FetchType.EAGER)
        private Games games1;

        @JsonBackReference(value="games-resources2")
        @OneToOne(mappedBy = "story_image_id", fetch = FetchType.EAGER)
        private Games games2;

        @JsonBackReference(value="paths-resources1")
        @OneToOne(mappedBy = "image_id", fetch = FetchType.EAGER)
        private Paths paths1;

        @JsonBackReference(value="paths-resources2")
        @OneToOne(mappedBy = "story_image_id", fetch = FetchType.EAGER)
        private Paths paths2;

        @JsonBackReference(value="scenes-resources1")
        @OneToOne(mappedBy = "image_id", fetch = FetchType.EAGER)
        private Scenes scenes1;

        @JsonBackReference(value="scenes-resources2")
        @OneToOne(mappedBy = "story_image_id", fetch = FetchType.EAGER)
        private Scenes scenes2;

        @JsonBackReference(value="resources-container")
        @ManyToOne
        @JoinColumn(name="container_id", referencedColumnName="id")
        private Container container;

        @JsonBackReference(value="resources-repositories")
        @ManyToOne
        @JoinColumn(name="repository_id", referencedColumnName="id")
        private Repositories repositories;

        @JsonIgnore
        @Lob
        private byte[] data;

        public Resources() {

        }

        public Resources(String resource_name, String content_type, byte[] data) {
            this.resource_name = resource_name;
            this.content_type = content_type;
            this.data = data;
        }

        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getResource_name() {
            return resource_name;
        }

        public void setResource_name(String resource_name) {
            this.resource_name = resource_name;
        }

        public String getContent_type() {
            return content_type;
        }

        public void setContent_type(String content_type) {
            this.content_type = content_type;
        }

        public long getSize() {
            return size;
        }

        public void setSize(long size) {
            this.size = size;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Games getGames1() {
        return games1;
    }

        public void setGames1(Games games1) {
        this.games1 = games1;
    }

        public Container getContainer() {
            return container;
        }

        public void setContainer(Container container) {
            this.container = container;
        }

        public Repositories getRepositories() {
            return repositories;
        }

        public void setRepositories(Repositories repositories) {
            this.repositories = repositories;
        }

        public byte[] getData() {
            return data;
        }

        public void setData(byte[] data) {
            this.data = data;
        }

    @JsonIgnore
    public Paths getPaths() {
        return paths1;
    }

    public void setPaths(Paths paths1) {
        this.paths1 = paths1;
    }

    public Games getGames2() {
        return games2;
    }

    public void setGames2(Games games2) {
        this.games2 = games2;
    }

    @JsonIgnore
    public Paths getPaths2() {
        return paths2;
    }

    public void setPaths2(Paths paths2) {
        this.paths2 = paths2;
    }

    public Scenes getScenes1() {
        return scenes1;
    }

    public void setScenes1(Scenes scenes1) {
        this.scenes1 = scenes1;
    }

    public Scenes getScenes2() {
        return scenes2;
    }

    public void setScenes2(Scenes scenes2) {
        this.scenes2 = scenes2;
    }
}

