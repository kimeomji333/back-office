package com.sparta.lv3backoffice.domain.dto.tutor;

import com.sparta.lv3backoffice.domain.entity.Tutor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TutorRequestDto {
    private String tutorName;
    private Long experienceYears;
    private String company;
    private String phoneNumber;
    private String bio;

    private boolean manager = false;

    public Tutor toEntity() {
        return Tutor.builder()
                .tutorName(tutorName)
                .experienceYears(experienceYears)
                .company(company)
                .phoneNumber(phoneNumber)
                .bio(bio)
                .build();
    }
}
