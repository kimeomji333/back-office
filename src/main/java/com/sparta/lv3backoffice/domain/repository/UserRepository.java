package com.sparta.lv3backoffice.domain.repository;


import com.sparta.lv3backoffice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 관리자 리포지토리 // UserService.java 에서 회원 중복 확인 부분
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
