package com.sparta.lv3backoffice.domain.dto.user;

import com.sparta.lv3backoffice.domain.entity.Department;
import com.sparta.lv3backoffice.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupResponseDto {
    private String username;
    private String password;
    private Department department;
    private String email;

    public SignupResponseDto(User user) {
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.department = user.getDepartment();
        this.password = user.getPassword();
    }
}
