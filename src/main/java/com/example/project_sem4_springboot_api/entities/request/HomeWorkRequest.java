package com.example.project_sem4_springboot_api.entities.request;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class HomeWorkRequest {
    private List<ImageDTO> images;
    private String title;
    private String content;
    private Date dueDate;
    private Long teacherSchoolYearClassSubject;

}
