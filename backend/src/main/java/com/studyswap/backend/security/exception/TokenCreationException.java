package com.studyswap.backend.security.exception;

import org.springframework.security.core.AuthenticationException;

public class TokenCreationException extends AuthenticationException {
    public TokenCreationException(String msg) {
        super(msg);
    }

    public TokenCreationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
