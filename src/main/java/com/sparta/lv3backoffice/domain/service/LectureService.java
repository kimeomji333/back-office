package com.sparta.lv3backoffice.domain.service;

import com.sparta.lv3backoffice.domain.dto.lecture.LectureRequestDto;
import com.sparta.lv3backoffice.domain.dto.lecture.LectureResponseDto;
import com.sparta.lv3backoffice.domain.dto.tutor.TutorRequestDto;
import com.sparta.lv3backoffice.domain.dto.tutor.TutorResponseDto;
import com.sparta.lv3backoffice.domain.entity.Lecture;
import com.sparta.lv3backoffice.domain.entity.Tutor;
import com.sparta.lv3backoffice.domain.entity.UserRoleEnum;
import com.sparta.lv3backoffice.domain.repository.LectureRepository;
import com.sparta.lv3backoffice.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final JwtUtil jwtUtil;

    // 강의 등록
    public LectureResponseDto registerLecture(LectureRequestDto lectureRequestDto, @RequestHeader("Authorization") String token) {

        // 강의 등록
        Lecture lecture = lectureRepository.save(lectureRequestDto.toEntity());
        return new LectureResponseDto(lecture);
    }

    // 선택한 강의 정보 수정 (MANAGER 만 가능)
    @Transactional
    public LectureResponseDto updateLecture(Long lectureId, LectureRequestDto lectureRequestDto, @RequestHeader("Authorization") String token) {

        // 강의가 DB에 존재하는지 확인
        Lecture lecture = lectureRepository.findByLectureId(lectureId).orElseThrow(() ->
                new IllegalArgumentException("선택한 강의는 존재하지 않습니다."));

        lecture.update(lectureRequestDto);
        return new LectureResponseDto(lecture);
    }

    // 선택 강의 조회
    public LectureResponseDto getLecture(Long lectureId, @RequestHeader("Authorization") String token) {

        // 강의가 DB에 존재하는지 확인
        Lecture lecture = lectureRepository.findByLectureId(lectureId).orElseThrow(() ->
                new IllegalArgumentException("선택한 강의는 존재하지 않습니다."));

        return new LectureResponseDto(lecture);
    }

    // 카테고리별 강의 목록 조회
    public List<LectureResponseDto> getLecturesByCategory(String category, String token) {

        List<Lecture> lectures = lectureRepository.findByCategoryOrderByCreatedAtDesc(category);

        // 해당 카테고리에 강의가 DB에 존재하는지 확인 후 반환
        return lectures.stream().map(LectureResponseDto::new).collect(Collectors.toList());
    }
}


