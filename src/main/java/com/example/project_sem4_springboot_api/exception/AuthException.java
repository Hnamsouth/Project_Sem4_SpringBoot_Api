package com.example.project_sem4_springboot_api.exception;


public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
