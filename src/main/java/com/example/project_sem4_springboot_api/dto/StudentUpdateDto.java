package com.example.project_sem4_springboot_api.dto;

import com.example.project_sem4_springboot_api.entities.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentUpdateDto extends StudentDto{

    @NotNull(message = "Id không được để trống !!!")
    private Long id;

    @Override
    @JsonIgnore
    public Student toStudent() {
        Student news = super.toStudent();
        news.setId(id);
        return news;
    }
}
