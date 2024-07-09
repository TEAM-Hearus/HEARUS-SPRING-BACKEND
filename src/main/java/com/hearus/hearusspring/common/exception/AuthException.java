package com.hearus.hearusspring.common.exception;

public class AuthException extends RuntimeException {

    public static final String ILLEGAL_REGISTRATION_ID = "Illegal Registration ID";

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

}
