package com.sparta.lv3backoffice.domain.repository;

import com.sparta.lv3backoffice.domain.entity.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
@EnableJpaRepositories
public interface TutorRepository extends JpaRepository<Tutor, Long> {
    Optional<Tutor> findByTutorId(Long tutorId);
}
