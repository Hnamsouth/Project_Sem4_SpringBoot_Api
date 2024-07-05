package com.example.project_sem4_springboot_api.entities.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageDTO {
    private MultipartFile file;
    private String tag;
    private String folderName;

}
