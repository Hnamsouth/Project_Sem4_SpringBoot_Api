package com.example.project_sem4_springboot_api.entities.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class TestUploadFile {
    public MultipartFile file;
    public String name;
}
