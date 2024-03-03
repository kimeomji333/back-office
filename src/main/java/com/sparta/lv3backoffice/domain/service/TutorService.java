package com.sparta.lv3backoffice.domain.service;

import com.sparta.lv3backoffice.domain.dto.tutor.TutorRequestDto;
import com.sparta.lv3backoffice.domain.dto.tutor.TutorResponseDto;
import com.sparta.lv3backoffice.domain.entity.Lecture;
import com.sparta.lv3backoffice.domain.entity.Tutor;
import com.sparta.lv3backoffice.domain.entity.UserRoleEnum;
import com.sparta.lv3backoffice.domain.repository.LectureRepository;
import com.sparta.lv3backoffice.domain.repository.TutorRepository;
import com.sparta.lv3backoffice.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;


// 강사 관련 서비스
@Service

public class TutorService {
    private final TutorRepository tutorRepository;
    private final LectureRepository lectureRepository;
    private final JwtUtil jwtUtil;


    private final String MANAGER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public TutorService(TutorRepository tutorRepository, LectureRepository lectureRepository, JwtUtil jwtUtil) {
        this.tutorRepository = tutorRepository;
        this.lectureRepository = lectureRepository;
        this.jwtUtil = jwtUtil;
    }

    // 강사 등록
    public TutorResponseDto registerTutor(TutorRequestDto tutorRequestDto, @RequestHeader("Authorization") String token) {

        // 강사 등록
        Tutor tutor = tutorRepository.save(tutorRequestDto.toEntity());
        return new TutorResponseDto(tutor);
    }


    // 선택한 강사 정보 수정
    @Transactional
    public TutorResponseDto updateTutor(Long tutorId, TutorRequestDto tutorRequestDto, @RequestHeader("Authorization") String token) {

        // 강사가 DB 에 존재하는지 확인
        Tutor tutor = tutorRepository.findTutorId(tutorId).orElseThrow(() ->
                new IllegalArgumentException("선택한 강사는 존재하지 않습니다."));

        tutor.update(tutorRequestDto);
        return new TutorResponseDto(tutor);
    }


    // 선택 강사 조회
    public TutorResponseDto getTutor(Long tutorId, @RequestHeader("Authorization") String token) {

        // 강사가 DB 에 존재하는지 확인
        Tutor tutor = tutorRepository.findById(tutorId).orElseThrow(() ->
                new IllegalArgumentException("선택한 강사는 존재하지 않습니다."));

        return new TutorResponseDto(tutor);
    }
}

