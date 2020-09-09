package com.example.springsecurity.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "repository")
public class Repositories {


    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid", strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "uuid" )
    private UUID id;

    private String name;

    @ManyToOne
    @JsonBackReference(value="user-repositories")
    @JoinColumn(name="owner_id", referencedColumnName="id")
    private User user;

    @JsonManagedReference(value="resources-repositories")
    @OneToMany(targetEntity=Resources.class, fetch = FetchType.EAGER, mappedBy="repositories", cascade = CascadeType.REMOVE)
    private Set<Resources> resources = new HashSet<>();

    @JsonManagedReference
    @OneToMany(targetEntity=Container.class, fetch = FetchType.EAGER, mappedBy="repositories", cascade = CascadeType.REMOVE)
    private Set<Container> containers = new HashSet<>();


    public Repositories() {
    }

    public Repositories(String name) {
        this.name = name;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Resources> getResources() {
        return resources;
    }

    public void setResources(Set<Resources> resources) {
        this.resources = resources;
    }

    public Set<Container> getContainers() {
        return containers;
    }

    public void setContainers(Set<Container> containers) {
        this.containers = containers;
    }

}
