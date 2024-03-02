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