package com.sparta.lv3backoffice.global.security;

import com.sparta.lv3backoffice.global.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 인가 필터
// 들어온 jwt 직접 검증, 직접 인가해주는 필터 (jwt 문제 없어, 너 허가!)
@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    // jwt 주입
    private final JwtUtil jwtUtil;
    // 사용자 존재 유무 체크
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // OncePerRequestFilter 상속 받으면, HttpServletRequest req, HttpServletResponse res 받아올 수 있어. 그냥 상속을 받았구나 간단히 생각하기.
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getJwtFromHeader(req);

        if (StringUtils.hasText(tokenValue)) {  // hasText 사용해서 있는지 확인

            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Token Error");
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());  // 토큰 만들 때, Subject 에 유저 이름 넣었으니, setAuthentication 파라미터에 username 넘겨줌.
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // setAuthentication : 인증 처리 메서드
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext(); // SecurityContextHolder 에 의해 context 생성
        Authentication authentication = createAuthentication(username); // createAuthentication 메서드로 authentication 로 구현제가 반환이되면,
        context.setAuthentication(authentication);  // 구현체를 setAuthentication 에 담음

        SecurityContextHolder.setContext(context); // context 를 다시 SecurityContextHolder 에 담음
    }

    // createAuthentication : 인증 객체 생성 하는 메서드
    // 매니저가 Authentication 인증 객체 만들고
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);  // UserDetailsServiceImpl.java 에서 만든 loadUserByUsername 호출
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // (principal, credentials, authorities) 각각의 자리

        // userDetailsService 에 loadUserByUsername 호출하면서, 인증 객체 userDetails 생성할
    }
}
