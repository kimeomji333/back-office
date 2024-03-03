package com.sparta.lv3backoffice.domain.controller;


import com.sparta.lv3backoffice.domain.dto.tutor.TutorRequestDto;
import com.sparta.lv3backoffice.domain.dto.tutor.TutorResponseDto;
import com.sparta.lv3backoffice.domain.entity.Lecture;
import com.sparta.lv3backoffice.domain.entity.Tutor;
import com.sparta.lv3backoffice.domain.entity.UserRoleEnum;
import com.sparta.lv3backoffice.domain.service.TutorService;
import com.sparta.lv3backoffice.global.exception.NotFoundException;
import com.sparta.lv3backoffice.global.exception.UnauthorizedException;
import com.sparta.lv3backoffice.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
            TutorResponseDto responseDto = tutorService.registerTutor(tutorRequestDto, token);
            return ResponseEntity.ok(responseDto);
        });
    }

    // 선택한 강사 정보 수정
    //@Secured(UserRoleEnum.Authority.MANAGER) // MANAGER 용
    @PutMapping("/tutor/{tutorId}")
    public ResponseEntity<?> updateTutor(@PathVariable Long tutorId, @RequestBody TutorRequestDto tutorRequestDto, @RequestHeader("Authorization") String token) {
        return handleRequest(() -> {
            TutorResponseDto responseDto = tutorService.updateTutor(tutorId, tutorRequestDto, token);
            return ResponseEntity.ok(responseDto);
        });
    }

    // 선택 강사 조회
    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<?> getTutor(@PathVariable Long tutorId, @RequestHeader("Authorization") String token) {
        return handleRequest(() -> {
            TutorResponseDto responseDto =  tutorService.getTutor(tutorId, token);
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