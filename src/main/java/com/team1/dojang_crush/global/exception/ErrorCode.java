package com.team1.dojang_crush.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_REQUEST(HttpStatus.BAD_REQUEST),
    NOT_FOUNT_POST(HttpStatus.NOT_FOUND),
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND),
    NOT_FOUNT_GROUP(HttpStatus.NOT_FOUND);
    private HttpStatus httpStatus;
}
