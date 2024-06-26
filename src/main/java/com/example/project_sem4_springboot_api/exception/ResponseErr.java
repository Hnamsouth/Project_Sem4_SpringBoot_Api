package com.example.project_sem4_springboot_api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class ResponseErr{
    private OffsetDateTime timestamp;
    private int statusCode;
    private String message;
    private String path;
    private String error;
//    private List<ResponseErrDetail> errorDetails;
}
