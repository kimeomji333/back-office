package com.sparta.lv3backoffice.domain.entity;

import com.sparta.lv3backoffice.domain.dto.lecture.LectureRequestDto;
import com.sparta.lv3backoffice.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lectures")
public class Lecture extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutor;

    @Column(nullable = false)
    private String tutorName;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Long price;

    public void update(LectureRequestDto lectureRequestDto) {
        this.tutorName = lectureRequestDto.getTutorName();
        this.title = lectureRequestDto.getTitle();
        this.description = lectureRequestDto.getDescription();
        this.category = lectureRequestDto.getCategory();
        this.price = lectureRequestDto.getPrice();
    }
}
