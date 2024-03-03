package com.sparta.lv3backoffice.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


// 회원정보UserInfo = 로그인 정보 : 로그인, 비밀번호
@Getter
@Setter
public class LoginRequestDto {
    private String email;
    private String password;


}