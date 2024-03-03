package com.sparta.lv3backoffice.domain.dto.user;

import com.sparta.lv3backoffice.domain.entity.Department;
import com.sparta.lv3backoffice.domain.entity.User;
import com.sparta.lv3backoffice.domain.entity.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,15}$")
    private String password;
    private Department department;
    @Email
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+$")
    private String email;
    private String adminToken = "";


    public User toEntity(UserRoleEnum role, String password) {
        return User.builder()
                .username(username)
                .email(email)
                .password(password)
                .department(department)
                .role(role)
                .build();
    }
}
