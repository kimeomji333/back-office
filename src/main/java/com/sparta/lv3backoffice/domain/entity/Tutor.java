package com.sparta.lv3backoffice.domain.entity;

import com.sparta.lv3backoffice.domain.dto.tutor.TutorRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tutors")
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tutorId;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL)
    private List<Lecture> lectureList = new ArrayList<>();

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

    public void update(TutorRequestDto tutorRequestDto) {
        this.tutorName = tutorRequestDto.getTutorName();
        this.experienceYears = tutorRequestDto.getExperienceYears();
        this.company = tutorRequestDto.getCompany();
        this.phoneNumber = tutorRequestDto.getPhoneNumber();
        this.bio = tutorRequestDto.getBio();
    }
}
