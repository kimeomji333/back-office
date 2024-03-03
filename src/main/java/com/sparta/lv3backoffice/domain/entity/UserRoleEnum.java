package com.sparta.lv3backoffice.domain.entity;

import lombok.Getter;

@Getter
// 사용자의 권한을 관리하는 enum class // MANAGER, STAFF 권한
public enum UserRoleEnum {
    STAFF(Authority.STAFF),  // 사용자 권한 USER -> STAFF
    MANAGER(Authority.MANAGER);  // 관리자 권한 ADMIN -> MANAGER

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String STAFF = "ROLE_STAFF";
        public static final String MANAGER = "ROLE_MANAGER";
    }
}