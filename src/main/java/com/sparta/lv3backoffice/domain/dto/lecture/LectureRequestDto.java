package com.sparta.lv3backoffice.domain.dto.lecture;

import com.sparta.lv3backoffice.domain.entity.Tutor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureRequestDto {
    private String tutorName;
    private String title;
    private String description;
    private String category;
    private Long price;

    private boolean manager = false;
    private String managerToken = "";
}
