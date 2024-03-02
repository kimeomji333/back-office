package com.sparta.lv3backoffice.domain.dto.lecture;

import com.sparta.lv3backoffice.domain.entity.Lecture;
import lombok.Getter;

@Getter
public class LectureResponseDto {
    private String tutorName;
    private String title;
    private String description;
    private String category;
    private Long price;

    public LectureResponseDto(Lecture lecture) {
        this.tutorName = lecture.getTutorName();
        this.title = lecture.getTitle();
        this.description = lecture.getDescription();
        this.category = lecture.getCategory();
        this.price = lecture.getPrice();
    }
}
