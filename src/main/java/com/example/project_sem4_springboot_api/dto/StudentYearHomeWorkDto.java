package com.example.project_sem4_springboot_api.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class StudentYearHomeWorkDto {
    private Long id;
    private Long homeWorkId;
    private Long studentId;
    private Long studentYearInfoId;
    private String description;
    private List<String> imageUrls;
    private List<MultipartFile> imageFiles;
    private Date submitTime;
    private boolean status;
    private String statusName;
    private Double point;


    public StudentYearHomeWorkDto(){

    }

    public StudentYearHomeWorkDto(Long id, Long homeWorkId, Long studentId, Long studentYearInfoId, String description, List<String> imageUrls, List<MultipartFile> imageFiles, Date submitTime, boolean status, String statusName, Double point) {
        this.id = id;
        this.homeWorkId = homeWorkId;
        this.studentId = studentId;
        this.studentYearInfoId = studentYearInfoId;
        this.description = description;
        this.imageUrls = imageUrls;
        this.imageFiles = imageFiles;
        this.submitTime = submitTime;
        this.status = status;
        this.statusName = statusName;
        this.point = point;
    }
}
