package com.rodtech.qideasauthapi.exception;

public class RegisteredEmailException extends RuntimeException {
    public RegisteredEmailException(String message) {
        super(message);
    }
}
