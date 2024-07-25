package com.team1.dojang_crush.global.exception;

import javax.naming.AuthenticationException;
import lombok.Getter;

@Getter
public class JWTAuthenticationException extends AuthenticationException {
    public JWTAuthenticationException(String message) {
        super(message);
    }
}
