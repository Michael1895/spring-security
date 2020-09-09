package com.example.springsecurity.repository;

import com.example.springsecurity.models.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RepositoriesRepository extends JpaRepository<Repositories, Long> {

    @Transactional
    Optional<Repositories> findById(UUID repositoriesId);

    @Transactional
    List<Repositories> findAllById(UUID id);

    @Transactional
    List<Repositories> findAll();

    @Transactional
    @Modifying
    void deleteById(UUID repositoriesId);

    Boolean existsById(UUID repositoriesId);
}
