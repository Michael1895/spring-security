package com.example.springsecurity.repository;

import com.example.springsecurity.models.Games;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GamesRepository extends JpaRepository<Games, Long>  {

    @Transactional
    Optional<Games> findById(UUID gamesId);

    @Transactional
    List<Games> findAllById(UUID id);

    @Transactional
    List<Games> findAll();

    @Transactional
    @Modifying
    void deleteById(UUID gamesId);

    Boolean existsById(UUID gamesId);
}
