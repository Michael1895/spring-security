package com.example.springsecurity.repository;

import com.example.springsecurity.models.Resources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResourcesRepository extends JpaRepository<Resources, String> {

    @Transactional
    Optional<Resources> findById(UUID fileId);

    Boolean existsById(UUID fileId);

    @Transactional
    @Modifying
    void deleteById(UUID fileId);
}
