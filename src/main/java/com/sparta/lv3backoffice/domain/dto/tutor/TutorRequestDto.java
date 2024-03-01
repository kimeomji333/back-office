package com.sparta.lv3backoffice.domain.dto.tutor;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TutorRequestDto {
    // 로그인을 통해 발급받은 JWT가 함께 요청
    private String tutorName;
    private Long experienceYears;
    private String company;
    private String phoneNumber;
    private String bio;

    private boolean manager = false;
    private String managerToken = "";

}
