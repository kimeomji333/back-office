package com.sparta.lv3backoffice.domain.controller;

import com.sparta.lv3backoffice.domain.dto.user.LoginRequestDto;
import com.sparta.lv3backoffice.domain.dto.user.SignupRequestDto;
import com.sparta.lv3backoffice.domain.service.UserService;
import com.sparta.lv3backoffice.global.exception.NotFoundException;
import com.sparta.lv3backoffice.global.exception.UnauthorizedException;
import com.sparta.lv3backoffice.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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
    private final AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    public UserController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
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
        if (!fieldErrors.isEmpty()) {
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
    @PostMapping("/user/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        try {
            // 사용자 인증을 시도하고 성공하면 Authentication 객체를 반환
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
            );

            // 인증에 성공했으므로 JWT 토큰 생성
            String token = jwtUtil.createToken(loginRequestDto.getUsername(), null); // 권한은 null 로 설정하거나 필요에 따라 사용자의 역할 정보를 전달

            // JWT 토큰을 Response 의 헤더에 추가
            jwtUtil.addJwtToHeader(token, response);

            // 로그인 성공 메시지와 함께 토큰을 반환
            return ResponseEntity.ok("로그인 성공 및 토큰 발급되었습니다.");
        } catch (AuthenticationException e) {
            // 인증 실패 시 401 Unauthorized 에러 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: " + e.getMessage());
        }
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

