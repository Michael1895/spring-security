package com.example.springsecurity.repository;

import com.example.springsecurity.models.Answers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnswersRepository  extends JpaRepository<Answers, Long>{

    @Transactional
    Optional<Answers> findById(UUID answersId);

    @Transactional
    List<Answers> findAllById(UUID answersId);

    @Transactional
    List<Answers> findAll();

    @Transactional
    @Modifying
    void deleteById(UUID answersId);

    Boolean existsById(UUID answersId);
}