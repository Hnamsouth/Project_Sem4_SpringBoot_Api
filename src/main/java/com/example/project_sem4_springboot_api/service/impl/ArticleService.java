package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.ArticleDto;
import com.example.project_sem4_springboot_api.entities.Article;
import com.example.project_sem4_springboot_api.entities.Image;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.repositories.ArticleRepository;
import com.example.project_sem4_springboot_api.repositories.UserRepository;
import com.example.project_sem4_springboot_api.security.service.UserDetailsImpl;
import com.example.project_sem4_springboot_api.service.impl.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ImageService imageService;

    private final UserRepository userRepository;


    @Transactional
    public ResponseEntity<?> saveArticle(ArticleDto articleDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl currentUser = (UserDetailsImpl) auth.getPrincipal();
        // Upload images and get URLs
        List<String> imageUrls = imageService.upload(articleDto.getImages());
        // Find the user by userId
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() ->
                new RuntimeException("User not found"));

        // Create and save the article
        Article article = Article.builder()
                .title(articleDto.getTitle())
                .content(articleDto.getContent())
                .user(user)
                .build();

        List<Image> images = imageUrls.stream()
                .map(url -> Image.builder()
                        .url(url)
                        .article(article)
                        .build())
                .collect(Collectors.toList());

        article.setImages(images);
        var res = articleRepository.save(article).toRes();
        return ResponseEntity.ok(res);
    }



    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }
}
