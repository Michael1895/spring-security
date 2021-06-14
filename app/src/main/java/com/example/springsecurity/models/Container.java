package com.example.springsecurity.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "container")
public class Container{

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid", strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "uuid" )
    private UUID id;

    private String container_name;

    @JsonBackReference(value="user-containers")
    @ManyToOne
    @JoinColumn(name="owner_id", referencedColumnName="id")
    private User user;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="repository_id", referencedColumnName="id")
    private Repositories repositories;

    @JsonManagedReference(value="resources-container")
    @OneToMany(targetEntity=Resources.class, fetch = FetchType.EAGER, mappedBy="container", cascade = CascadeType.REMOVE)
    private Set<Resources> resources = new HashSet<>();

    public Container(String container_name) {
        this.container_name = container_name;
    }

    public Container() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContainer_name() {
        return container_name;
    }

    public void setContainer_name(String container_name) {
        this.container_name = container_name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Repositories getRepositories() {
        return repositories;
    }

    public void setRepositories(Repositories repositories) {
        this.repositories = repositories;
    }

    public Set<Resources> getResources() {
        return resources;
    }

    public void setResources(Set<Resources> resources) {
        this.resources = resources;
    }
}
