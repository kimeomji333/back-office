package com.sparta.lv3backoffice.domain.dto.user;

import com.sparta.lv3backoffice.domain.entity.Department;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    private String email;
    private String password;
}