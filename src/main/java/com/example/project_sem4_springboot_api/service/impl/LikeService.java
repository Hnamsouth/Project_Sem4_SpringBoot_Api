package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.entities.Article;
import com.example.project_sem4_springboot_api.entities.Like;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.repositories.ArticleRepository;
import com.example.project_sem4_springboot_api.repositories.LikeRepository;
import com.example.project_sem4_springboot_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    public List<Like> getAllLikes() {
        return likeRepository.findAll();
    }

    public Optional<Like> getLikeById(Long id) {
        return likeRepository.findById(id);
    }

    @Transactional
    public ResponseEntity<?> createAndDeleteLike(Long articleId) {
        var user = userRepository.findById(AuthService.getUserId()).orElseThrow();

        Article article = articleRepository.findById(articleId).orElseThrow(() ->
                new RuntimeException("Article not found"));
        var isLiked = likeRepository.findByArticle_IdAndUser_Id( article.getId(),user.getId());
        if (isLiked !=null) {
            likeRepository.delete(isLiked);
            return ResponseEntity.ok("Unlike");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                likeRepository.save(Like.builder().article(article).user(user).createdAt(new Date()).build())
        );
    }
    @Transactional
    public void deleteLike(Long id) {
        likeRepository.deleteById(id);
    }
}
