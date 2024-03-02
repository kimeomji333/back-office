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

import java.util.List;


// 강사 관련 서비스
@Service
@RequiredArgsConstructor
public class TutorService {
    private final TutorRepository tutorRepository;
    //private final LectureRepository lectureRepository;
    private final JwtUtil jwtUtil;

    private final String MANAGER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 강사 등록
    public TutorResponseDto registerTutor(TutorRequestDto tutorRequestDto, String token) {

        // 인증 : 토큰 확인
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }

        // 강사 등록
        Tutor tutor = new Tutor(tutorRequestDto);
        Tutor saveTutor = tutorRepository.save(tutor);
        TutorResponseDto tutorResponseDto = new TutorResponseDto(tutor);

        return tutorResponseDto;
    }


    // 선택한 강사 정보 수정
    public Long updateTutor(Long tutorId, TutorRequestDto tutorRequestDto, String token) {

        //
        // 인증 : 토큰 확인
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }

        // 인가 : 권한 확인
        UserRoleEnum role = UserRoleEnum.STAFF;  // 일반 사용자 권한을 넣어놓은다.
        if (tutorRequestDto.isManager()) {   // boolean type 은 is 로 시작함(규칙), isAdmin // (true)면 관리자 권한으로 회원가입
            if (!MANAGER_TOKEN.equals(tutorRequestDto.getManagerToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.MANAGER;  // 위에서 USER -> ADMIN 권한으로 덮어짐.
        }

        // 강사가 DB 에 존재하는지 확인
        Tutor tutor = findTutorId(tutorId);

        tutor.update(tutorRequestDto);

        return tutorId;
    }


    // 선택 강사 조회
    public TutorResponseDto getTutor(Long tutorId, String token) {

        // 인증 : 토큰 확인
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }

        // 강사가 DB 에 존재하는지 확인
        Tutor tutor = findTutorId(tutorId);

        return new TutorResponseDto(tutor);
    }


//------------------ 메서드 -------------------

    private Tutor findTutorId(Long tutorId) {
        return tutorRepository.findById(tutorId).orElseThrow(() ->
                new IllegalArgumentException("선택한 강사는 존재하지 않습니다.")
        );
    }
}

