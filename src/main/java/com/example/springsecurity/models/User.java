package com.example.springsecurity.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Entity
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") 
		})

public class User {

	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid", strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	@Type(type="pg-uuid")
	private UUID id;

	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	private String password;
	
	private String token;

	private LocalDate birthdate;

	private String full_name;

	private UUID oauthId;

	@Column(columnDefinition = "boolean default false")
	private boolean isEnabled;

	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime tokenCreationDate;

	@JsonManagedReference(value="user-resources")
	@OneToMany(targetEntity=Resources.class, fetch = FetchType.EAGER, mappedBy="user")
	private List<Resources> resources = new ArrayList<>();

	@Column(name = "avatar_id")
	private UUID avatarId;

	@JsonManagedReference(value="user-repositories")
	@OneToMany(targetEntity=Repositories.class, fetch = FetchType.EAGER, mappedBy="user", cascade = CascadeType.REMOVE)
	private Set<Repositories> repositories = new HashSet<>();

	@JsonManagedReference(value="user-containers")
	@OneToMany(targetEntity=Container.class, fetch = FetchType.EAGER, mappedBy="user", cascade = CascadeType.REMOVE)
	private Set<Container> containers = new HashSet<>();

	@JsonManagedReference(value="user-games")
	@OneToMany(targetEntity=Games.class, fetch = FetchType.EAGER, mappedBy="user", cascade = CascadeType.REMOVE)
	private Set<Games> games = new HashSet<>();

	@JsonManagedReference(value="users-annotation_answers")
	@OneToMany(targetEntity=AnnotationAnswers.class, fetch = FetchType.EAGER, mappedBy="user", cascade = CascadeType.REMOVE)
	private Set<AnnotationAnswers> annotation_answers = new HashSet<>();

	@JsonManagedReference(value="user-game_registrations")
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private Set<GameRegistrations> registrations = new HashSet<>();

	@JsonManagedReference(value="user-moves")
	@OneToMany(targetEntity=Moves.class, fetch = FetchType.EAGER, mappedBy="user", cascade = CascadeType.REMOVE)
	private Set<Moves> moves = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User() {
	}

	public User(String username, String email, String password) {
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	public String getFullName() {
		return full_name;
	}

	public void setFullName(String full_name) {
		this.full_name = full_name;
	}

	public UUID getOauthId() {
		return oauthId;
	}

	public void setOauthId(UUID oauthId) {
		this.oauthId = oauthId;
	}

	public LocalDateTime getTokenCreationDate() {
		return tokenCreationDate;
	}

	public void setTokenCreationDate(LocalDateTime tokenCreationDate) {
		this.tokenCreationDate = tokenCreationDate;
	}

	public List<Resources> getResources() {
		return resources;
	}

	public void setResources(List<Resources> resources) {
		this.resources = resources;
	}

	public UUID getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(UUID avatarId) {
        this.avatarId = avatarId;
    }

	public Set<Repositories> getRepositories() {
		return repositories;
	}

	public void setRepositories(Set<Repositories> repositories) {
		this.repositories = repositories;
	}

	public Set<Container> getContainers() {
		return containers;
	}

	public void setContainers(Set<Container> containers) {
		this.containers = containers;
	}

	public Set<Games> getGames() {
		return games;
	}

	public void setGames(Set<Games> games) {
		this.games = games;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Set<AnnotationAnswers> getAnnotation_answers() {
		return annotation_answers;
	}

	public void setAnnotation_answers(Set<AnnotationAnswers> annotation_answers) {
		this.annotation_answers = annotation_answers;
	}

	public Set<GameRegistrations> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(Set<GameRegistrations> registrations) {
		this.registrations = registrations;
	}

	public Set<Moves> getMoves() {
		return moves;
	}

	public void setMoves(Set<Moves> moves) {
		this.moves = moves;
	}
}
