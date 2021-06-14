package com.example.springsecurity.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.springsecurity.payload.Projections.AdminUserProjection;
import com.example.springsecurity.payload.Projections.UserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.example.springsecurity.models.User;

import javax.transaction.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	Optional<User> findByEmail(String email);

	Optional<User> findByToken(String token);

	Optional<User> findUsersById(UUID id);

	List<User> findAll();

	@Transactional
	@Modifying
	void deleteByUsername(String username);

	@Transactional
	@Modifying
	void deleteById(UUID id);

	List<UserProjection> getUserProjectionsBy();

	List<UserProjection> findById(UUID id);

	List<UserProjection> findUserByUsername(String username);

	List<AdminUserProjection> getAllBy();

	List<AdminUserProjection> findAgainById(UUID id);

	List<AdminUserProjection> findAgainUserByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);

	Boolean existsByToken(String token);

	Boolean existsById(UUID id);
}
