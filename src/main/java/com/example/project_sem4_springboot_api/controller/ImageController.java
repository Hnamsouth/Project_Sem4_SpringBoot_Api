package com.example.project_sem4_springboot_api.controller;

import com.example.project_sem4_springboot_api.service.impl.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/api/v1/image")
@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping
    public ResponseEntity<List<String>> upload(@RequestParam("files") List<MultipartFile> multipartFiles) {
        List<String> urls = imageService.upload(multipartFiles);
        return ResponseEntity.ok(urls);
    }
}
