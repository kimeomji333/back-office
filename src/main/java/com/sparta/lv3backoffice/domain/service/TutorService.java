package com.sparta.lv3backoffice.domain.service;

import com.sparta.lv3backoffice.domain.dto.tutor.TutorRequestDto;
import com.sparta.lv3backoffice.domain.dto.tutor.TutorResponseDto;
import com.sparta.lv3backoffice.domain.entity.Tutor;
import com.sparta.lv3backoffice.domain.repository.LectureRepository;
import com.sparta.lv3backoffice.domain.repository.TutorRepository;
import com.sparta.lv3backoffice.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
@RequiredArgsConstructor
public class TutorService {
    private final TutorRepository tutorRepository;

    // 강사 등록
    public TutorResponseDto registerTutor(TutorRequestDto tutorRequestDto, String token) {

        // 강사 등록
        Tutor tutor = tutorRepository.save(tutorRequestDto.toEntity());
        return new TutorResponseDto(tutor);
    }

    // 선택한 강사 정보 수정 (MANAGER 만 가능)
    @Transactional
    public TutorResponseDto updateTutor(Long tutorId, TutorRequestDto tutorRequestDto, String token) {

        // 강사가 DB 에 존재하는지 확인
        Tutor tutor = tutorRepository.findByTutorId(tutorId).orElseThrow(() ->
                new IllegalArgumentException("선택한 강사는 존재하지 않습니다."));

        tutor.update(tutorRequestDto);
        return new TutorResponseDto(tutor);
    }

    // 선택 강사 조회
    public TutorResponseDto getTutor(Long tutorId, String token) {

        // 강사가 DB 에 존재하는지 확인
        Tutor tutor = tutorRepository.findByTutorId(tutorId).orElseThrow(() ->
                new IllegalArgumentException("선택한 강사는 존재하지 않습니다."));

        return new TutorResponseDto(tutor);
    }
}

