package com.team1.dojang_crush.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_FOUND_USER(HttpStatus.NOT_FOUND),
    NOT_FOUND_OAUTH2_REGISTRATION_ID(HttpStatus.NOT_FOUND),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST),
    NOT_FOUND_POST(HttpStatus.NOT_FOUND),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND),
    NOT_FOUND_GROUP(HttpStatus.NOT_FOUND);

    private final HttpStatus status;
}
