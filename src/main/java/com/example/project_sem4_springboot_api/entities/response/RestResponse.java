package com.example.project_sem4_springboot_api.entities.response;

import lombok.Data;

@Data
public class RestResponse<T> {

    private int statusCode;

    private String error;

    private Object message;

    private T data;

}