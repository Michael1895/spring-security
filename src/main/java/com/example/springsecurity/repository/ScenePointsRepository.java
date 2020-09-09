package com.example.springsecurity.repository;

import com.example.springsecurity.models.ScenePoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScenePointsRepository extends JpaRepository<ScenePoints, Long> {

    @Transactional
    Optional<ScenePoints> findById(UUID scenePointsId);

    @Transactional
    List<ScenePoints> findAllById(UUID scenePointsId);

    @Transactional
    List<ScenePoints> findAll();

    @Transactional
    @Modifying
    void deleteById(UUID scenePointsId);

    Boolean existsById(UUID scenePointsId);

}
