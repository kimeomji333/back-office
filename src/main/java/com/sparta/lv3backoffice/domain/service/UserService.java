package com.sparta.lv3backoffice.domain.service;


// 로그인, 가입 서비스

import com.sparta.lv3backoffice.domain.dto.user.SignupRequestDto;
import com.sparta.lv3backoffice.domain.entity.User;
import com.sparta.lv3backoffice.domain.entity.UserRoleEnum;
import com.sparta.lv3backoffice.domain.repository.UserRepository;
import com.sparta.lv3backoffice.global.exception.NotFoundException;
import com.sparta.lv3backoffice.global.exception.UnauthorizedException;
import com.sparta.lv3backoffice.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    // 주입 받아옴
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ADMIN_TOKEN : 일반사용자인지 관리자인지. 토큰을 이용해서 관리자 권한을 준다.
    // 아래와 같이 ADMIN_TOKEN 을 주면, 해킹되서,
    // 현업에서는 '관리자'권한을 부여할 수 있는 관리자 페이지를 구현 페이지르 따로 구현하거나
    // 승인자에 의해 결제하는 과정으로 구현함.
    // 우리는 일단 관리자 토큰 넣어서 관리자로서 회원가입 할 수 있도록 권한 부여.
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 회원 가입
    public void signup(SignupRequestDto requestDto) {  // 회원가입할 데이터를 requestDto 로 받아와
        String username = requestDto.getUsername();  // requestDto 에서 getUsername 가져와 변수 username 에 담음.
        String password = passwordEncoder.encode(requestDto.getPassword());  // 평문을 암호화 해서 password 에 담음.

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);  // Optional 타입으로 받음. null 체크하기 위해 만들어진 타입(값이 없으면 NULL, 있다면 값을 넣어줌) // userRepository 에 findByUsername 쿼리 메서드 작성해보자.
        if (checkUsername.isPresent()) {   // isPresent : 현재 Optional 에 넣어준 값이 존재하는 지 확인 메서드
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String email = requestDto.getEmail();
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }

        // 사용자 ROLE 확인 (권한확인)
        UserRoleEnum role = UserRoleEnum.STAFF;  // 일반 사용자 권한을 넣어놓은다.
        if (requestDto.isAdmin()) {   // boolean type 은 is 로 시작함(규칙), isAdmin // (true)면 관리자 권한으로 회원가입
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.MANAGER;  // 위에서 USER -> ADMIN 권한으로 덮어짐.
        }

        // 사용자 department 확인
        String department = requestDto.getDepartment();

        // 사용자 등록
        User user = new User(username, password, email, department, role);  // 등록하려면 user entity 클래스 객체를 만듦 : JPA 에서 Entity class 객체 하나가 DB의 한 열과 같다. (안의 내용은 생성자) 생성자를 통해서 만듦. 빨간 밑줄 뜨면 Create Constructor ^^
        userRepository.save(user);
    }

    // 로그인
    public ResponseEntity<?> login(String email, String password) {

        // 이메일로 사용자 정보 조회
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new NotFoundException("등록된 사용자가 없습니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("비밀번호가 일치하지 않습니다.");
        }

        // JwtUtil 클래스를 사용해서 JWT 토큰 생성
        String token = jwtUtil.createToken(user.getUsername(), user.getRole());

        // 발급한 토큰을 Header 에 추가하여 로그인 성공 확인을 함께 반환
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        // 사용자 정보와 토큰을 함께 반환
        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("token", token);

        return new ResponseEntity<>(response, headers, HttpStatus.OK);
    }
}