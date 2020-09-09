package com.example.springsecurity.repository;

import com.example.springsecurity.models.Paths;
import com.example.springsecurity.payload.Projections.PathsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PathsRepository extends JpaRepository<Paths, Long> {
    @Transactional
    Optional<Paths> findPathsById(UUID pathsId);

    @Transactional
    PathsProjection findById(UUID id);

    @Transactional
    List<Paths> findAllById(UUID id);

    @Transactional
    List<Paths> findAll();

    @Transactional
    @Modifying
    void deleteById(UUID pathsId);

    @Transactional
    List<PathsProjection> findPathsProjectionsBy();

    @Transactional
    List<PathsProjection> findPathsProjectionById(UUID pathId);

    Boolean existsById(UUID pathsId);
}