package com.sparta.lv3backoffice.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CategoryEnum {
    NODE("Node"), SPRING("Spring"), REACT("React");

    private final String value;
}
