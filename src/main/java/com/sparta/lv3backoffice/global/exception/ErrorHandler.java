package com.sparta.lv3backoffice.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandler {

    public ResponseEntity<?> forbiddenError() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}