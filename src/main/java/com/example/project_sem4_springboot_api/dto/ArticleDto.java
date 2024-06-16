package com.example.project_sem4_springboot_api.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Data
public class ArticleDto {
    // Getters and setters
    private String title;
    private String content;
    private List<MultipartFile> images;


}
