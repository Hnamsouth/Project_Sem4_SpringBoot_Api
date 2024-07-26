package com.example.project_sem4_springboot_api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class HomeWorkDetailDto {

    private Long id;
    private String title;
    private String content;
    private Date dueDate;
    private List<String> imageUrls;
    private List<StudentYearHomeWorkDto> studentYearHomeWorks;

    public HomeWorkDetailDto() {
    }

    public HomeWorkDetailDto(Long id, String title, String content, Date dueDate, List<String> imageUrls, List<StudentYearHomeWorkDto> studentYearHomeWorks) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dueDate = dueDate;
        this.imageUrls = imageUrls;
        this.studentYearHomeWorks = studentYearHomeWorks;
    }
}
