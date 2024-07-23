package com.team1.dojang_crush.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_REQUEST(400, "유효하지 않은 요청입니다"),
    NOT_FOUND_POST(404, "포스트를 찾을 수 없습니다"),
    NOT_FOUND_COMMENT(404, "댓글을 찾을 수 없습니다"),
    NOT_FOUND_MEMBER(404, "회원을 찾을 수 없습니다"),

    // Oauth2, JWT
    ILLEGAL_REGISTRATION_ID(500, "잘못된 registrationId입니다."),
    INVALID_TOKEN(401, "잘못된 토큰입니다."),
    NO_AUTHORIZATION(401, "인증 정보를 가지고 있지 않습니다."),
    INVALID_JWT_SIGNATURE(401, "토큰이 만료되었습니다."),

    // Member
    MEMBER_NOT_FOUND(404, "해당 사용자를 찾을 수 없습니다.");

    private final int status;
    private final String message;
}