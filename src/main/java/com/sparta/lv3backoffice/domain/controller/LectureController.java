package com.sparta.lv3backoffice.domain.controller;

import com.sparta.lv3backoffice.domain.dto.lecture.LectureRequestDto;
import com.sparta.lv3backoffice.domain.dto.lecture.LectureResponseDto;
import com.sparta.lv3backoffice.domain.entity.Lecture;
import com.sparta.lv3backoffice.domain.entity.UserRoleEnum;
import com.sparta.lv3backoffice.domain.service.LectureService;
import com.sparta.lv3backoffice.global.exception.NotFoundException;
import com.sparta.lv3backoffice.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LectureController {

    private final LectureService lectureService;

    // 강의 등록
    @PostMapping("/lecture")
    public ResponseEntity<?> registerLecture(@RequestBody LectureRequestDto lectureRequestDto, @RequestHeader("Authorization") String token) {
        return handleRequest(() -> {
            LectureResponseDto responseDto = lectureService.registerLecture(lectureRequestDto, token);
            return ResponseEntity.ok(responseDto);
        });
    }

    // 선택한 강의 정보 수정 (MANAGER 만 가능)
    @Secured(UserRoleEnum.Authority.MANAGER)
    @Transactional
    @PutMapping("/lecture/{lectureId}")
    public ResponseEntity<?> updateLecture(@PathVariable Long lectureId, @RequestBody LectureRequestDto lectureRequestDto, @RequestHeader("Authorization") String token) {
        return handleRequest(() -> {
            LectureResponseDto responseDto = lectureService.updateLecture(lectureId, lectureRequestDto, token);
            return ResponseEntity.ok(responseDto);
        });
    }

    // 선택 강의 조회
    @GetMapping("/lecture/{lectureId}")
    public ResponseEntity<?> getLecture(@PathVariable Long lectureId, @RequestHeader("Authorization") String token) {
        return handleRequest(() -> {
            LectureResponseDto responseDto = lectureService.getLecture(lectureId, token);
            return ResponseEntity.ok(responseDto);
        });
    }

    // 선택한 강사가 촬영한 강의 목록 조회
    @GetMapping("/lecturesByTutor/{tutorId}")
    public ResponseEntity<List<LectureResponseDto>> getLecturesByTutor(@PathVariable Long tutorId, @RequestHeader("Authorization") String token) {
        List<LectureResponseDto> lectureList = lectureService.getLecturesByTutor(tutorId, token);
        return ResponseEntity.ok(lectureList);
    }


    // 카테고리별 강의 목록 조회
    @GetMapping("/lecture/category/{category}")
    public ResponseEntity<?> getLectureByCategory(@PathVariable String category, @RequestHeader("Authorization") String token) {
        return handleRequest(() -> {
            List<LectureResponseDto> responseDto = lectureService.getLecturesByCategory(category, token);
            return ResponseEntity.ok(responseDto);
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
