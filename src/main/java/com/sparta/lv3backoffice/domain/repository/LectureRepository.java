package com.sparta.lv3backoffice.domain.repository;

import com.sparta.lv3backoffice.domain.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
@Repository
@EnableJpaRepositories
public interface LectureRepository extends JpaRepository<Lecture, Long>{
    Optional<Lecture> findByLectureId(Long lectureId);
    List<Lecture> findByTutorTutorIdOrderByCreatedAtDesc(Long tutorId);
    List<Lecture> findByCategoryOrderByCreatedAtDesc(String category);
}
