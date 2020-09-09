package com.example.springsecurity.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springsecurity.models.User;
import com.example.springsecurity.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

		return UserDetailsImpl.build(user);
	}

	public UserDetails loadUserByEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new Exception("User not found with email: " + email));

		return UserDetailsImpl.build(user);
	}

	public UserDetails loadUserByToken(String token) throws Exception {
		User user = userRepository.findByToken(token)
				.orElseThrow(() -> new Exception("User not found with token: " + token));

		return UserDetailsImpl.build(user);
	}


}
