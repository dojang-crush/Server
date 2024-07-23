package com.team1.dojang_crush.global.jwt;

import com.team1.dojang_crush.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenException extends RuntimeException {
    private final ErrorCode errorCode;
}
