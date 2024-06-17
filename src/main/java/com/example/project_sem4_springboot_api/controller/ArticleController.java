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
    public ResponseEntity<Article> uploadArticle(
            @RequestPart("article") String articleString,
            @RequestPart("images") List<MultipartFile> images,
            @RequestParam("user_id") Long userId) throws IOException {

        // Convert articleString to ArticleDto
        ArticleDto articleDto = objectMapper.readValue(articleString, ArticleDto.class);

        // Set images in ArticleDto
        articleDto.setImages(images);

        // Save article data along with image URLs and user to the database
        Article savedArticle = articleService.saveArticle(articleDto, userId);

        // Return the saved article's details
        return ResponseEntity.ok(savedArticle);
    }

    @GetMapping
    public ResponseEntity<List<Article>> getAllArticles() {
        List<Article> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }
}
