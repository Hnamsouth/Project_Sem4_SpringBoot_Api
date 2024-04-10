package com.example.project_sem4_springboot_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseErrDetail {
    private Integer errorCode;
    private String errorMessage;
    private String referenceUrl;
}
