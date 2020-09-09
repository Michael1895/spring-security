package com.example.springsecurity.repository;

import com.example.springsecurity.models.GameRegistrations;
import com.example.springsecurity.models.Games;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GameRegistrationsRepository extends JpaRepository<GameRegistrations, Long> {
    @Transactional
    Optional<GameRegistrations> findById(UUID registrationsId);

    @Transactional
    Optional<GameRegistrations> findByUserId(UUID user_id);

    @Transactional
    Optional<GameRegistrations> findByGameId(UUID game_id);

    @Transactional
    List<GameRegistrations> findAllByGameId(UUID game_id);

    @Transactional
    List<GameRegistrations> findAllById(UUID id);

    @Transactional
    List<GameRegistrations> findAll();

    @Transactional
    @Modifying
    void deleteById(UUID registrationsId);

    Boolean existsById(UUID registrationsId);
}
