package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.entities.Article;
import com.example.project_sem4_springboot_api.entities.Comment;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.repositories.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment addComment(Long articleId, Long userId, String content) {
        Comment comment = new Comment();
        comment.setArticle(new Article(articleId));
        comment.setUser(new User(userId));
        comment.setContent(content);
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByArticleId(Long articleId) {
        return commentRepository.findAllByArticleId(articleId);
    }
}
