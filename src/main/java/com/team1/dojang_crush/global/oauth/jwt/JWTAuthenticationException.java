package com.team1.dojang_crush.global.oauth.jwt;

import lombok.Getter;

import javax.naming.AuthenticationException;

@Getter
public class JWTAuthenticationException extends AuthenticationException {
    public JWTAuthenticationException(String message) {
        super(message);
    }
}
