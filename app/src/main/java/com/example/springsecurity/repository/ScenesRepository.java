package com.example.springsecurity.repository;

import com.example.springsecurity.models.Games;
import com.example.springsecurity.models.Scenes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScenesRepository extends JpaRepository<Scenes, Long> {

    @Transactional
    Optional<Scenes> findById(UUID scenesId);

    @Transactional
    List<Scenes> findAllById(UUID id);

    @Transactional
    List<Scenes> findAll();

    @Transactional
    @Modifying
    void deleteById(UUID scenesId);

    Boolean existsById(UUID scenesId);
}
