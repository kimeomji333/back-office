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

import java.util.List;

@Service
@RequiredArgsConstructor
// 강의 관련 서비스
public class LectureService {
    private final LectureRepository lectureRepository;
    private final JwtUtil jwtUtil;

    private final String MANAGER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 강의 등록
    public LectureResponseDto registerLecture(LectureRequestDto lectureRequestDto, String token) {
        // 인증 : 토큰 확인
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }

        // 강의 등록
        Lecture lecture = new Lecture(lectureRequestDto);
        Lecture saveLecture = lectureRepository.save(lecture);
        LectureResponseDto lectureResponseDto = new LectureResponseDto(lecture);

        return lectureResponseDto;
    }

    // 선택한 강의 정보 수정
    public Long updateLecture(Long lectureId, LectureRequestDto lectureRequestDto, String token) {

        // 인증 : 토큰 확인
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }

        // 인가 : 권한 확인
        UserRoleEnum role = UserRoleEnum.STAFF;  // 일반 사용자 권한을 넣어놓은다.
        if (lectureRequestDto.isManager()) {   // boolean type은 is 로 시작함(규칙), isAdmin // (true)면 관리자 권한으로 회원가입
            if (!MANAGER_TOKEN.equals(lectureRequestDto.getManagerToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.MANAGER;  // 위에서 USER -> ADMIN 권한으로 덮어짐.
        }

        // 강의가 DB에 존재하는지 확인
        Lecture lecture = findLectureId(lectureId);

        lecture.update(lectureRequestDto);

        return lectureId;
    }

    // 선택 강의 조회
    public LectureResponseDto getTutor(Long lectureId, String token) {

        // 인증 : 토큰 확인
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }

        // 강의가 DB에 존재하는지 확인
        Lecture lecture = findLectureId(lectureId);

        return new LectureResponseDto(lecture);
    }

    // 카테고리별 강의 목록 조회
    public List<Lecture> getLecturesByCategory(String category, String token) {

        // 인증 : 토큰 확인
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Token Error");
        }

        // 해당 카테고리에 강의가 DB에 존재하는지 확인 후 반환
        return (List<Lecture>) lectureRepository.findByCategoryOrderByCreatedAtDesc(category);
    }

    private Lecture findLectureId(Long lectureId) {
        return lectureRepository.findById(lectureId).orElseThrow(() ->
                new IllegalArgumentException("선택한 강의는 존재하지 않습니다.")
        );
    }
}
