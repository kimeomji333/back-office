package com.sparta.lv3backoffice.domain.entity;

import com.sparta.lv3backoffice.domain.dto.lecture.LectureRequestDto;
import com.sparta.lv3backoffice.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
@Table(name = "lectures")
public class Lecture extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long LectureId;

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutor tutorId;

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


    public Lecture(Tutor tutorId, String title, String description, String category, Long price) {
        this.tutorId = tutorId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.price = price;
    }

    public Lecture(LectureRequestDto lectureRequestDto) {
        this.tutorName = lectureRequestDto.getTutorName();
        this.title = lectureRequestDto.getTitle();
        this.description = lectureRequestDto.getDescription();
        this.category = lectureRequestDto.getCategory();
        this.price = lectureRequestDto.getPrice();
    }

    public void update(LectureRequestDto lectureRequestDto) {
        this.tutorName = lectureRequestDto.getTutorName();
        this.title = lectureRequestDto.getTitle();
        this.description = lectureRequestDto.getDescription();
        this.category = lectureRequestDto.getCategory();
        this.price = lectureRequestDto.getPrice();
    }
}
