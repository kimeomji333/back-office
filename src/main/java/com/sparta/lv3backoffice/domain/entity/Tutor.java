package com.sparta.lv3backoffice.domain.entity;

import com.sparta.lv3backoffice.global.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tutors")
public class Tutor extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tutorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Column(nullable = false)
    private String tutorName;

    @Column(nullable = false)
    private Long experienceYears;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String bio;

    public Tutor(Lecture lecture, String tutorName, Long experienceYears, String company, String phoneNumber, String bio) {
        this.lecture = lecture;
        this.tutorName = tutorName;
        this.experienceYears = experienceYears;
        this.company = company;
        this.phoneNumber = phoneNumber;
        this.bio = bio;
    }
}
