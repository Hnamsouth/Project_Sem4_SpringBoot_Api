package com.example.project_sem4_springboot_api.exception;

public class DataExistedException extends RuntimeException {
    public DataExistedException(String message) {
        super(message);
    }
}