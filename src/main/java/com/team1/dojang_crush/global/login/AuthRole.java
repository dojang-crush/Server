package com.team1.dojang_crush.global.login;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthRole { // Oauth2에서 사용

    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "사용자");

    private final String key;
    private final String title;
}