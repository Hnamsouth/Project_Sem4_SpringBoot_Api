package com.example.project_sem4_springboot_api.controller;

//import com.example.project_sem4_springboot_api.dto.ArticleDto;
import com.example.project_sem4_springboot_api.dto.ArticleDto;
import com.example.project_sem4_springboot_api.entities.Article;
import com.example.project_sem4_springboot_api.entities.HomeWork;
import com.example.project_sem4_springboot_api.service.impl.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequestMapping("/api/v1/articles")
@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;


    @PostMapping(consumes = {"multipart/form-data"},value="")
    public ResponseEntity<Article> createArticle(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam Long createId,
            @RequestParam List<MultipartFile> images) throws ParseException, IOException, ExecutionException, InterruptedException {
        Article saveArticle = articleService.saveArticle(title, content, createId, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveArticle);
    }

    @GetMapping
    public ResponseEntity<List<ArticleDto>> getAllArticles() {
        List<ArticleDto> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }
}
