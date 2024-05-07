package com.example.project_sem4_springboot_api.dto;

import com.example.project_sem4_springboot_api.entities.request.RegisterRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class StudentParentCreateDto extends RegisterRequest {

    @NotEmpty
    private List<Long> studentList;

}
