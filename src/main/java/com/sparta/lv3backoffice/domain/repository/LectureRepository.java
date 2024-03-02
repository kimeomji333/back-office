package com.sparta.lv3backoffice.domain.repository;

import com.sparta.lv3backoffice.domain.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface LectureRepository extends JpaRepository <Lecture, Long>{
    Collection<Lecture> findByCategoryOrderByCreatedAtDesc(String category);
}
