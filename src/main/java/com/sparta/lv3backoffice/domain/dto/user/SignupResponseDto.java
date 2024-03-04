package com.sparta.lv3backoffice.domain.dto.user;

import com.sparta.lv3backoffice.domain.entity.Department;
import com.sparta.lv3backoffice.domain.entity.User;
import com.sparta.lv3backoffice.domain.entity.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponseDto {
    private Long id;
    private String username;
    private String password;
    private Department department;
    private String email;
    private UserRoleEnum role;

    public SignupResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.department = user.getDepartment();
        this.password = user.getPassword();
        this.role = user.getRole();
    }
}
