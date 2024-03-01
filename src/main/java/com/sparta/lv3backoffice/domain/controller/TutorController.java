package com.sparta.lv3backoffice.domain.controller;


import com.sparta.lv3backoffice.domain.dto.tutor.TutorRequestDto;
import com.sparta.lv3backoffice.domain.entity.Lecture;
import com.sparta.lv3backoffice.domain.entity.Tutor;
import com.sparta.lv3backoffice.domain.service.TutorService;
import com.sparta.lv3backoffice.global.exception.NotFoundException;
import com.sparta.lv3backoffice.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

// 강사 관련 컨트롤러
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TutorController {

    private final TutorService tutorService;

    // 강사 등록
    @PostMapping("/tutor")
    public ResponseEntity<?> registerTutor(@RequestBody TutorRequestDto tutorRequestDto, @RequestHeader("Authorization") String token) {
        return handleRequest(() -> {
            tutorService.registerTutor(tutorRequestDto, token);
            return ResponseEntity.ok(" 성공적으로 강사 등록이 완료되었습니다.");
        });
    }

    // 선택한 강사 정보 수정
    @PutMapping("/tutor/{tutorId}")
    public ResponseEntity<?> updateTutor(@PathVariable Long tutorId, @RequestBody TutorRequestDto tutorRequestDto, @RequestHeader("Authorization") String token) {
        return handleRequest(() -> {
            tutorService.updateTutor(tutorId, tutorRequestDto, token);
            return ResponseEntity.ok("성공적으로 강사 수정이 완료되었습니다.");
        });
    }

    // 선택 강사 조회
    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<?> getTutor(@PathVariable Long tutorId, @RequestHeader("Authorization") String token) {
        return handleRequest(() -> {
            tutorService.getTutor(tutorId, token);
            return ResponseEntity.ok("성공적으로 강사 조회가 완료되었습니다.");
        });
    }


    // 선택한 강사가 촬영한 강의 목록 조회
    @GetMapping("/tutor/{tutorId}/courses")
    public ResponseEntity<?> getLectureByTutorId(@PathVariable Long tutorId, @RequestBody Tutor tutor, @RequestHeader("Authorization") String token) {
        return handleRequest(() -> {
           List<Lecture> lectures = tutorService.getTutorLectures(tutorId, token);
           return ResponseEntity.ok(lectures);
        });
    }


    // ------------------ handleRequest 메서드 --------
    private ResponseEntity<?> handleRequest(Supplier<ResponseEntity<?>> supplier) {
        try {
            return supplier.get();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인터넷 서버 오류: " + e.getMessage());
        }
    }

}

//    강사 등록 기능
//    이름, 경력(년차), 회사, 전화번호, 소개를 저장할 수 있습니다.
//    로그인을 통해 발급받은 JWT가 함께 요청됩니다.
//    관리자만 강사 등록이 가능합니다.
//    등록된 강사의 정보를 반환 받아 확인할 수 있습니다.

//    선택한 강사의 경력, 회사, 전화번호, 소개를 수정할 수 있습니다.
//    로그인을 통해 발급받은 JWT가 함께 요청됩니다.
//    MANAGER 권한을 가진 관리자만 강사 정보 수정이 가능합니다.
//    수정된 강사의 정보를 반환 받아 확인할 수 있습니다.


        // 선택한 강사가 촬영한 강의 목록 조회
//    선택한 강사가 촬영한 강의를 조회할 수 있습니다.
//    로그인을 통해 발급받은 JWT가 함께 요청됩니다.
//    관리자만 강의 조회가 가능합니다.
//    조회된 강의 목록은 등록일 기준 내림차순으로 정렬 되어있습니다

// <?>은 와일드카드 제네릭스 : 어떤 타입이든 될 수 있지만 정확한 타입은 중요하지 않다
// Spring의 ResponseEntity는 제네릭 타입을 사용하여 응답 본문의 타입을 지정할 수 있다.