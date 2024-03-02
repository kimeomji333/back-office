package com.sparta.lv3backoffice.domain.dto.tutor;

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
    private String managerToken = "";

}
