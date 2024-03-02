package com.sparta.lv3backoffice.global.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.lv3backoffice.domain.dto.user.LoginRequestDto;
import com.sparta.lv3backoffice.domain.entity.UserRoleEnum;
import com.sparta.lv3backoffice.global.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import static com.sparta.lv3backoffice.global.jwt.JwtUtil.AUTHORIZATION_HEADER;

// 인증 필터
@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // 생성자 주입으로 받아옴
    private final JwtUtil jwtUtil;

    // 생성자 호출 시 setFilterProcessesUrl 도 같이 호출해서 url path 를 주자.
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/user/login"); // 상속 받으면, setFilterProcessesUrl 메서드 사용 가능.
    }

    // 메서드 3개 오버라이딩
    // attemptAuthentication : 로그인 시도하느 메서드
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");

        // ObjectMapper().readValue :  json 형태의 String 데이터를 Object 로 바꾸는 것
        // 첫 번째 파라미터인 request.getInputStream() 로 요청값 바디부분에 username, ps 가 json 형식으로 넘어옴,  두 번째 파라미터인 변환할 오브젝트 타입으로 LoginRequestDto.class 사용
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);  // 변수 requestDto 로 반환 받음

            return getAuthenticationManager().authenticate(  // 상속을 받았으니 사용가능 authenticate 메서드 : 인증처리함
                    new UsernamePasswordAuthenticationToken( // 토큰은 만들어서 값을 각각 get 해옴. 권한은 null
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }


    // successfulAuthentication 메서드 : 인증 성공 시 메서드 실행 -> 는 파라미터로 Authentication 인증객체를 받아옴. authResult 안에 UserDetailsImpl 들어 있음
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("로그인 성공 및 JWT 생성");

        // ProductController.java 에서 @AuthenticationPrincipal 해서 UserDetailsImpl 받아 온 것이
        // 여기선 내부적으로 UserDetailsImpl 를 담아와 -> 여기선 ((UserDetailsImpl) authResult.getPrincipal()) 코드로 작성.
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(username, role);
        response.addHeader(AUTHORIZATION_HEADER, token); // addJwtToCookie 메서드 : 쿠키 생성하고, 받아온 response 객체에 넣어줌
    }

    // unsuccessfulAuthentication : 만약 인증 실패 시 메서드 실행
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("로그인 실패");
        response.setStatus(401); // Status Code.인증이 되지 않았습니다. // 추가적인 작업이 필요하다면 여기에서 작성 ! ! ! !
    }
}