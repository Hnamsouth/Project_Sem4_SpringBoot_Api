package com.example.project_sem4_springboot_api.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {
    private final Path rootLocation;

    public StorageService(@Value("${upload.root-location}") String location) {
        this.rootLocation = Paths.get(location);
    }

    public String storeFile(MultipartFile file) {
        // Implement file storage logic, e.g., save to disk and return the URL
        String fileName = file.getOriginalFilename();
        Path filePath = rootLocation.resolve(fileName);
        // Save the file to disk
        // Return the URL of the stored file
        return "/uploads/" + fileName;
    }
}
