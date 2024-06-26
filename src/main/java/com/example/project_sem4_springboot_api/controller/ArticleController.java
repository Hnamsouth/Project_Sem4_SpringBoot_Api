package com.example.project_sem4_springboot_api.controller;

import com.example.project_sem4_springboot_api.dto.ArticleDto;
import com.example.project_sem4_springboot_api.entities.Article;
import com.example.project_sem4_springboot_api.service.impl.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/v1/articles")
@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ObjectMapper objectMapper;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadArticle(
            @RequestPart("article") String articleString,
            @RequestPart("images") List<MultipartFile> images) throws IOException {

        // Convert articleString to ArticleDto
        ArticleDto articleDto = objectMapper.readValue(articleString, ArticleDto.class);

        // Set images in ArticleDto
        articleDto.setImages(images);

        // Return the saved article's details
        return articleService.saveArticle(articleDto);
    }

    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }
}
