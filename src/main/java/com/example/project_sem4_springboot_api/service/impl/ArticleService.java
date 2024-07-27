package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.ArticleDto;
import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.repositories.ArticleRepository;
import com.example.project_sem4_springboot_api.repositories.FileStorageRepository;
import com.example.project_sem4_springboot_api.repositories.UserRepository;
import com.example.project_sem4_springboot_api.security.service.UserDetailsImpl;
import com.example.project_sem4_springboot_api.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final FileStorageRepository fileStorageRepository;



    private final UserRepository userRepository;
    private final String ARTICLE_TAG= "article";
    private final String ARTICLE_FN= "article";
    private final CloudinaryService cloudinaryService;





    public Article saveArticle(
            String title,
            String content,
            Long createdId,
            List<MultipartFile> images
    ) throws IOException, ExecutionException, InterruptedException {
        User user = userRepository.findById(createdId).orElseThrow(() ->
                new RuntimeException("User not found"));

       Article article = new Article();
       article.setTitle(title);
       article.setContent(content);
       article.setUser(user);
       article.setCreatedAt(new Date());
        var saveArticle = articleRepository.save(article);
        if(!images.isEmpty()){
            cloudinaryService.uploadMultiImage(images,ARTICLE_TAG + saveArticle.getId(), ARTICLE_FN);
            saveArticle.setUrl(ARTICLE_TAG + saveArticle.getId());
            articleRepository.save(saveArticle);
        }
        return  saveArticle;
    }


    public ResponseEntity<?> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        var tags = articles.stream().map(Article::getUrl).toList();

        var listImagesUrl = cloudinaryService.getImageGroupByTags(tags);
        var rs= articles.stream().map(s->{
            var ar = s.convertToDto();
//            ar.put("articleImageUrls",listImagesUrl.get(s.getUrl()));
            ar.setArticleImageUrls(listImagesUrl.get(s.getUrl()));
            return ar;
        }).toList();

        return ResponseEntity.ok(rs);
    }

}
