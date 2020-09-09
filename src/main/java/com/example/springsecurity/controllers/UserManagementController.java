package com.example.springsecurity.controllers;

import com.example.springsecurity.models.*;
import com.example.springsecurity.payload.DTO.UserDTO;
import com.example.springsecurity.payload.Projections.UserProjection;
import com.example.springsecurity.payload.response.MessageResponse;
import com.example.springsecurity.repository.*;
import com.example.springsecurity.security.services.ResourcesStorageService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")

public class UserManagementController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	ResourcesStorageService resourcesStorageService;

	@Autowired
	ResourcesRepository resourcesRepository;

	@Autowired
	RepositoriesRepository repositoriesRepository;

	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}


	@GetMapping("/users")
	@Transactional
	public List<? extends UserProjection> printUsers(Authentication authentication) {

		try {
			boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
			if (hasAuthority) {
				return userRepository.getAllBy();
			}
			return userRepository.getUserProjectionsBy();

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
		}
	}

	@Transactional
	@GetMapping("/users/{userId}")
	public List<? extends UserProjection> printUser(@PathVariable UUID userId, Authentication authentication) {

		if (!userRepository.existsById(userId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id does not exist.");
		}

		try {
			boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
			if (hasAuthority) {
				return userRepository.findAgainById(userId);
			}
			return userRepository.findById(userId);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
		}
	}

	@GetMapping("/users/me")
	public List<? extends UserProjection> printUser(Authentication authentication) {
		try {
			boolean hasAuthority = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
			if (hasAuthority) {
				return userRepository.findAgainUserByUsername(authentication.getName());
			}
			return userRepository.findUserByUsername(authentication.getName());

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authorization is required." , e);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/users")
	public ResponseEntity<?> createUser(@RequestBody UserDTO dto) {
		if (userRepository.existsByUsername(dto.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (dto.getUsername() == null) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Invalid username."));
		}

		if (dto.getPassword() == null) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Invalid password."));
		}

		if (dto.getEmail() == null) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("You need to specify an email."));
		}

		if (userRepository.existsByEmail(dto.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		boolean valid = EmailValidator.getInstance(true).isValid(dto.getEmail()); //check email validity
		if (!valid) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: The email is not in a valid format."));
		}

		// Create new user's account
		User user = new User(dto.getUsername(),
				dto.getEmail(),
				encoder.encode(dto.getPassword()));

		Set<String> strRoles = dto.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role playerRole = roleRepository.findByName(ERole.ROLE_PLAYER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(playerRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
					case "admin":
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);

						break;
					case "creator":
						Role creatorRole = roleRepository.findByName(ERole.ROLE_CREATOR)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(creatorRole);

						break;
					default:
						Role playerRole = roleRepository.findByName(ERole.ROLE_PLAYER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(playerRole);
				}
			});
		}

		user.setRoles(roles);

		if(dto.getFullName() != null) {
			user.setFullName(dto.getFullName());
		}

		if(dto.getBirthdate() != null) {
			user.setBirthdate(dto.getBirthdate());
		}

		user.setIsEnabled(true);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("Account successfully created."));
	}


	@DeleteMapping("users/me")
	public ResponseEntity<?> deleteUser(Authentication authentication) {
		try {
			userRepository.deleteByUsername(authentication.getName());
			return ResponseEntity.ok(new MessageResponse("Your account is successfully deleted."));
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authentication is required." , e);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
		if (!userRepository.existsById(userId)) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: User does not exist."));
		}
			userRepository.deleteById(userId);
			return ResponseEntity.ok(new MessageResponse("Your account is successfully deleted."));
	}


	@PatchMapping(value = "users/me", consumes = "application/json")
	@Transactional
	public ResponseEntity<?> updateUser(@RequestBody UserDTO dto, Authentication authentication) {
		try {
			User user = userRepository.findByUsername(authentication.getName()).get();

			if(dto.getAvatar_id() != null) {
				if(!resourcesRepository.existsById(dto.getAvatar_id())) {
					return ResponseEntity
							.badRequest()
							.body(new MessageResponse("Error: Avatar does not exist."));
				}
				user.setAvatarId(dto.getAvatar_id());
			}

			if(dto.getFullName() != null) {
				user.setFullName(dto.getFullName());
			}

			if(dto.getBirthdate() != null) {
				user.setBirthdate(dto.getBirthdate());
			}

			if(dto.getEmail() != null) {
				user.setEmail(dto.getEmail());
			}

			userRepository.save(user);
			return ResponseEntity.ok(new MessageResponse("Your account is successfully updated."));

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Full authentication is required." , e);
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PatchMapping(value = "users/{userId}", consumes = "application/json")
	public ResponseEntity<?> updateUser(@RequestBody UserDTO dto, @PathVariable UUID userId) {

		if (!userRepository.existsById(userId)) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Id does not exist."));
		}

		User user = userRepository.findUsersById(userId).get();

		if(dto.getFullName() != null) {
			user.setFullName(dto.getFullName());
		}

		if(dto.getBirthdate() != null) {
			user.setBirthdate(dto.getBirthdate());
		}

		if(dto.getEmail() != null) {
			user.setEmail(dto.getEmail());
		}

		if(dto.getPassword() != null) {
			user.setPassword(encoder.encode(dto.getPassword()));
		}

		if(dto.getUsername() != null) {
			user.setUsername(dto.getUsername());
		}


		if(dto.getRole() != null) {

			Set<String> strRoles = dto.getRole();
			Set<Role> roles = new HashSet<>();

			strRoles.forEach(role -> {
				switch (role) {
					case "admin":
						Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(adminRole);

						break;
					case "creator":
						Role creatorRole = roleRepository.findByName(ERole.ROLE_CREATOR)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(creatorRole);

						break;
					default:
						Role playerRole = roleRepository.findByName(ERole.ROLE_PLAYER)
								.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
						roles.add(playerRole);
				}
			});
			user.setRoles(roles);
		}

		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("Your account is successfully updated."));
	}

}
