package com.sparta.lv3backoffice.domain.controller;

import com.sparta.lv3backoffice.domain.dto.lecture.LectureRequestDto;
import com.sparta.lv3backoffice.domain.dto.tutor.TutorRequestDto;
import com.sparta.lv3backoffice.domain.entity.Lecture;
import com.sparta.lv3backoffice.domain.entity.Tutor;
import com.sparta.lv3backoffice.domain.service.LectureService;
import com.sparta.lv3backoffice.global.exception.NotFoundException;
import com.sparta.lv3backoffice.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

// 강의 관련 컨트롤러
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LectureController {
    private final LectureService lectureService;

    // 강의 등록
    @PostMapping("/lecture")
    public ResponseEntity<?> registerLecture(@RequestBody LectureRequestDto lectureRequestDto, @RequestHeader("Authorization") String token) {
        return handleRequest(() -> {
            lectureService.registerLecture(lectureRequestDto, token);
            return ResponseEntity.ok(" 성공적으로 강의 등록이 완료되었습니다.");
        });
    }

    // 선택한 강의 정보 수정
    @PutMapping("/lecture/{lectureId}")
    public ResponseEntity<?> updateLecture(@PathVariable Long lectureId, @RequestBody LectureRequestDto lectureRequestDto, @RequestHeader("Authorization") String token) {
        return handleRequest(() -> {
            lectureService.updateLecture(lectureId, lectureRequestDto, token);
            return ResponseEntity.ok("성공적으로 강의 수정이 완료되었습니다.");
        });
    }

    // 선택 강의 조회
    @GetMapping("/lecture/{lectureId}")
    public ResponseEntity<?> getLecture(@PathVariable Long lectureId, @RequestHeader("Authorization") String token) {
        return handleRequest(() -> {
            lectureService.getTutor(lectureId, token);
            return ResponseEntity.ok("성공적으로 강의 조회가 완료되었습니다.");
        });
    }

    // 카테고리별 강의 목록 조회
    @GetMapping("/lecture/{category}")
    public ResponseEntity<?> getLectureByCategory(@PathVariable String category, @RequestBody Lecture lecture, @RequestHeader("Authorization") String token) {
        return handleRequest(() -> {
            List<Lecture> lectures = lectureService.getLecturesByCategory(category, token);
            return ResponseEntity.ok(lectures);
        });
    }

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
