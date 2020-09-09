package com.example.springsecurity.repository;

import com.example.springsecurity.models.Questions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionsRepository extends JpaRepository<Questions, Long> {

    @Transactional
    Optional<Questions> findById(UUID questionsId);

    @Transactional
    List<Questions> findAllById(UUID questionsId);

    @Transactional
    List<Questions> findAll();

    @Transactional
    @Modifying
    void deleteById(UUID questionsId);

    Boolean existsById(UUID questionsId);
}
