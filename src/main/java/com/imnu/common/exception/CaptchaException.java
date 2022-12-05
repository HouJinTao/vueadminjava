package com.imnu.common.exception;


import org.springframework.security.core.AuthenticationException;

public class CaptchaException extends AuthenticationException {

    public CaptchaException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public CaptchaException(String msg) {
        super(msg);
    }
}
