package com.example.springsecurity.repository;

import com.example.springsecurity.models.Container;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ContainerRepository extends JpaRepository<Container, Long> {

    @Transactional
    Optional<Container> findById(UUID id);

    @Transactional
    List<Container> findAllById(UUID id);

    @Transactional
    List<Container> findAll();

    @Transactional
    @Modifying
    void deleteById(UUID id);

    Boolean existsById(UUID id);
}

