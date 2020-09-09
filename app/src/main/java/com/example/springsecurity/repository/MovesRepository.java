package com.example.springsecurity.repository;

import com.example.springsecurity.models.Moves;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MovesRepository extends JpaRepository<Moves, Long> {

    @Transactional
    Optional<Moves> findById(UUID movesId);

    @Transactional
    List<Moves> findAllById(UUID id);

    @Transactional
    List<Moves> findAllByUserId(UUID userId);

    @Transactional
    List<Moves> findAll();

    @Transactional
    @Modifying
    void deleteById(UUID movesId);

    Boolean existsById(UUID movesId);
}
