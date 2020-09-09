package com.example.springsecurity.repository;

import com.example.springsecurity.models.Annotations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnnotationsRepository extends JpaRepository<Annotations, Long> {

    @Transactional
    Optional<Annotations> findById(UUID annotationsId);

    @Transactional
    List<Annotations> findAllById(UUID id);

    @Transactional
    List<Annotations> findAll();

    @Transactional
    @Modifying
    void deleteById(UUID annotationsId);

    Boolean existsById(UUID annotationsId);
}
