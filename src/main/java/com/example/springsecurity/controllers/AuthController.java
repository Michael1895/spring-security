package com.example.springsecurity.controllers;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.example.springsecurity.payload.DTO.ResetPasswordDTO;
import com.example.springsecurity.payload.request.Email;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.springsecurity.models.ERole;
import com.example.springsecurity.models.Role;
import com.example.springsecurity.models.User;
import com.example.springsecurity.payload.request.LoginRequest;
import com.example.springsecurity.payload.request.SignupRequest;
import com.example.springsecurity.payload.response.JwtResponse;
import com.example.springsecurity.payload.response.MessageResponse;
import com.example.springsecurity.repository.RoleRepository;
import com.example.springsecurity.repository.UserRepository;
import com.example.springsecurity.security.jwt.JwtUtils;
import com.example.springsecurity.security.services.UserDetailsImpl;

import org.springframework.mail.SimpleMailMessage;
import java.time.LocalDateTime;
import java.time.Duration;
import com.example.springsecurity.security.services.EmailService;
import com.example.springsecurity.payload.DTO.ConfirmationDTO;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class AuthController {
	
	private static final long EXPIRE_TOKEN_AFTER_MINUTES = 15;
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@Autowired
	private EmailService emailService;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt,
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		boolean valid = EmailValidator.getInstance(true).isValid(signUpRequest.getEmail()); //check email validity
		if (!valid) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: The email is not in a valid format."));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
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
		
		user.setToken(UUID.randomUUID().toString().replace("-","").substring(0,6));
		user.setTokenCreationDate(LocalDateTime.now());
		
		userRepository.save(user);

		// Email message 
			SimpleMailMessage AccountConfirmation = new SimpleMailMessage();
			AccountConfirmation.setFrom("support@demo.com");
			AccountConfirmation.setTo(signUpRequest.getEmail());
			AccountConfirmation.setSubject("Account Confirmation");
			AccountConfirmation.setText("Validation token: " + user.getToken() + "\nUse the token given in the validation form to activate your account.");

			emailService.sendEmail(AccountConfirmation);
			
		return ResponseEntity.ok(new MessageResponse("Registered successfully, please check your emails to activate your account."));
	}


	@GetMapping("/resend")
	public ResponseEntity<?> resend(@Valid @RequestBody Email email) {

		if (!userRepository.existsByEmail(email.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email does not exist."));
		}

		// Get user
		User user = userRepository.findByEmail(email.getEmail()).get();

		user.setToken(UUID.randomUUID().toString().replace("-","").substring(0,6));
		user.setTokenCreationDate(LocalDateTime.now());

		userRepository.save(user);

		// Email message
		SimpleMailMessage AccountConfirmation = new SimpleMailMessage();
		AccountConfirmation.setFrom("support@demo.com");
		AccountConfirmation.setTo(email.getEmail());
		AccountConfirmation.setSubject("Account Confirmation");
		AccountConfirmation.setText("Validation token: " + user.getToken() + "\nUse the token given in the validation form to activate your account.");

		emailService.sendEmail(AccountConfirmation);

		return ResponseEntity.ok(new MessageResponse("Token resent successfully, please check your emails to activate your account."));
	}


	@PutMapping(value = "/confirm", consumes = "application/json")
	public ResponseEntity<?> confirmAccount(@Valid @RequestBody ConfirmationDTO dto) {

		if (!userRepository.existsByEmail(dto.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("User Not Found with email: " + dto.getEmail()));
		}

		if (!userRepository.existsByToken(dto.getToken())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Invalid token."));
		}

		User user = userRepository.findByToken(dto.getToken()).get();
		user.setIsEnabled(true);

		LocalDateTime tokenCreationDate = user.getTokenCreationDate();
		if (isTokenExpired(tokenCreationDate)) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Token has expired"));
		}

		user.setToken(null);
		user.setTokenCreationDate(null);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("Account confirmed successfully."));
	}

	@PostMapping(value = "/forgot", consumes = "application/json")
	public ResponseEntity<?> forgotPassword(@RequestBody Email email) {


		if (!userRepository.existsByEmail(email.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Invalid email."));
		}


		User user = userRepository.findByEmail(email.getEmail()).get();
		user.setToken(UUID.randomUUID().toString().replace("-","").substring(0,6));
		user.setTokenCreationDate(LocalDateTime.now());

		userRepository.save(user);

		// Email message
		SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
		passwordResetEmail.setFrom("support@demo.com");
		passwordResetEmail.setTo(user.getEmail());
		passwordResetEmail.setSubject("Password Reset Request");
		passwordResetEmail.setText("Reset token: " + user.getToken() + "\nUse the token given in the reset form to reset your password.");

		emailService.sendEmail(passwordResetEmail);
		return ResponseEntity.ok(new MessageResponse("A reset token has been sent." +
				" Please follow the instructions given to reset your password."));

	}

	@PutMapping(value = "/reset", consumes = "application/json")
	public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO dto) {

		if (!userRepository.existsByToken(dto.getToken())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Invalid token!"));
		}

		if (!userRepository.existsByEmail(dto.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email not found."));
		}

		if (dto.getPassword().isEmpty()) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Password is empty"));
		}

		User user = userRepository.findByToken(dto.getToken()).get();
		user.setPassword(encoder.encode(dto.getPassword()));

		LocalDateTime tokenCreationDate = user.getTokenCreationDate();

		if (isTokenExpired(tokenCreationDate)) {
			throw new RuntimeException("Token has expired");
		}

		user.setToken(null);
		user.setTokenCreationDate(null);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("Your password is successfully updated."));
	}

	private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

		LocalDateTime now = LocalDateTime.now();
		Duration diff = Duration.between(tokenCreationDate, now);

		return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
	}

}
