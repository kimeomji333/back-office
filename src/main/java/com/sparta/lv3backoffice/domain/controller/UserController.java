package com.sparta.lv3backoffice.domain.controller;

import com.sparta.lv3backoffice.domain.dto.user.LoginRequestDto;
import com.sparta.lv3backoffice.domain.dto.user.SignupRequestDto;
import com.sparta.lv3backoffice.domain.dto.user.SignupResponseDto;
import com.sparta.lv3backoffice.domain.entity.User;
import com.sparta.lv3backoffice.domain.repository.UserRepository;
import com.sparta.lv3backoffice.domain.service.UserService;
import com.sparta.lv3backoffice.global.exception.NotFoundException;
import com.sparta.lv3backoffice.global.exception.UnauthorizedException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.function.Supplier;

// 로그인, 가입 컨트롤러

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/user/login-page")
//    public String loginPage() {
//        return "login";
//    }
//
//    @GetMapping("/user/signup")
//    public String signupPage() {
//        return "signup";
//    }

    // 회원 가입
    @PostMapping("/user/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(!fieldErrors.isEmpty()) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
        }

        return handleRequest(() -> {
            userService.signup(requestDto);
            return ResponseEntity.ok("성공적으로 회원가입이 완료되었습니다.");
        });
    }

    // 로그인
//    @PostMapping("/user/login")
//    public ResponseEntity<?> login(@ResponseBody LoginRequestDto loginRequestDto) {
//        // 이메일 기반으로 사용자 조회
//
//
//    }


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

