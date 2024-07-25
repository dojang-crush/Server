package com.team1.dojang_crush.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorDto {
    private String timestamp;
    private HttpStatus status;
    private String error;
    private String message;
}