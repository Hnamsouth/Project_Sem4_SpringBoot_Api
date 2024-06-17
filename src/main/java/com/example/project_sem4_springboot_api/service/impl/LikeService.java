package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.entities.Article;
import com.example.project_sem4_springboot_api.entities.Like;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.repositories.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public void likeArticle(Long articleId, Long userId) {
        Like like = new Like();
        like.setArticle(new Article(articleId));
        like.setUser(new User(userId));
        likeRepository.save(like);
    }

    public long countLikes(Long articleId) {
        return likeRepository.countByArticleId(articleId);
    }
}
