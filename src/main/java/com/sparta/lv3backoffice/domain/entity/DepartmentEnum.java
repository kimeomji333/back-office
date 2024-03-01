package com.sparta.lv3backoffice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DepartmentEnum {
    CURRICULUM("커리큘럼 부서"), DEV("개발 부서"), MARKETING("마케팅 부서");

    private final String value;
}
