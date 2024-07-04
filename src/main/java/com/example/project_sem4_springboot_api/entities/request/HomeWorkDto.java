package com.example.project_sem4_springboot_api.entities.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class HomeWorkDto {
    private Long id;
    private List<MultipartFile> files;
    private List<String> imageUrls;
    private List<MultipartFile> imageFiles;
    private String title;
    private String description;
    private Date dueDate;
    private Long courseId;
    private boolean status;
    private String statusName;
    private double point;
    private Date submitTime;
    private String content;
    private Long teacherSchoolYearClassSubjectId;
    private int submittedCount;
    private int totalStudentCount;

    public HomeWorkDto() {
    }

    public HomeWorkDto(Long id, List<MultipartFile> files, List<String> imageUrls, List<MultipartFile> imageFiles, String title, String description, Date dueDate, Long courseId, boolean status, String statusName, double point, Date submitTime, String content, Long teacherSchoolYearClassSubjectId, int submittedCount, int totalStudentCount) {
        this.id = id;
        this.files = files;
        this.imageUrls = imageUrls;
        this.imageFiles = imageFiles;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.courseId = courseId;
        this.status = status;
        this.statusName = statusName;
        this.point = point;
        this.submitTime = submitTime;
        this.content = content;
        this.teacherSchoolYearClassSubjectId = teacherSchoolYearClassSubjectId;
        this.submittedCount = submittedCount;
        this.totalStudentCount = totalStudentCount;
    }
}
