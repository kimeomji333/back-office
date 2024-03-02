package com.sparta.lv3backoffice.global.jwt;

import com.sparta.lv3backoffice.domain.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

// JWT 생성 시 필요 데이터
@Slf4j(topic = "JwtUil")
@Component
public class JwtUtil {
    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";  // 쿠키의 NAME 값

    // 사용자 권한 값의 KEY (권한을 구분하기 위한 가져오기 위한 key 값을 줌)
    public static final String AUTHORIZATION_KEY = "auth";  // admin or user 권한에 대한 정보를 jwt 에 담아서 보낼 수 있다.

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";  // Bearer : 우리가 만든 Token 앞에 붙일 용어(규칙)

    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}")
    // Base64 Encode 한 SecretKey // application.properties 안에 있는 값 Value 애너테이션(in beans.factory)으로 가져옴
    private String secretKey;
    private Key key; // Secret key 담을 객체를 만듦. JWT 를 암호화하거나 복구해서 검증할 때 사용
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;  // 컨트롤 마우스로 들어가서 아무거나 사용


    // JwtUtil 클래스의 생성자를 호출한 뒤에 secretKey 를 key 필드에서 값을 담고, @PostConstruct 를 달고, init 메서드 하나 만들어서 코드 실행
    @PostConstruct  //  딱 한 번만 받아오면 되는 값을 사용 할 때마다 요청을 새로 호출하는 실수를 방지하기 위해 사용된다.
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);  // secretKey 값이 Base64 로 인코딩한 값이기에, 디코딩해주어야함.
        key = Keys.hmacShaKeyFor(bytes);  // Keys 클래스에 hmacShaKeyFor 메서드를 사용해줌. 그냥 그런 메서드만 있다고 알아둬.
    }

    // 사용자 권한을 관리하는 Enum 클래스를 만들어보자.

    //--------------------------------------------------------------------------------------
    // JWT 생성
    // 토큰 생성
    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +   // 아래 데이터(필요한 것 선택해서)와 암께 암호화가 된다. 토큰이 만들어지면서 BEARER_PREFIX 와 함께져 반환이 된다.
                Jwts.builder()  // Jwts 클래스에 builder 사용해서 쭉 아래와 같이 데이터를 넣는다. 마지막에 compact 하면 Jwt 토큰이 생성된다.
                        .setSubject(username) // 사용자 식별자값(ID) // username or PK 값을 넣으면 된다.
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한   // claim 에 키값, 벨류(권한) 값 넣을 수 있다.
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간 설정 = new Date(현재시간 + 만료시간)
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // secretKey, 암호화 알고리즘(HS256)
                        .compact();
    }


    // 생성된 JWT 를 Header 에 저장
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // JWT 검증
    // 토큰 검증
    public boolean validateToken(String token) {  // 자른 순수한 토큰을 받아와
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);  // 이 한 줄로 토큰의 위변조 체크
            return true;  // 문제가 없다면 true 를 반환
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;  // 여기까지 내려오면 오류가 있다는 뜻이니, false 를 반환
    }

    // JWT 에서 사용자 정보 가져오기
    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody(); // 마지막에 body 부분에 Claims 에 데이터들이 들어있는지 확인.  jwt 가 Claim 기반 웹토큰이니,
    }
}