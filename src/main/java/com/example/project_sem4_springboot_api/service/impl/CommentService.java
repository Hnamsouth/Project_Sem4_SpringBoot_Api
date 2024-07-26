package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.entities.Article;
import com.example.project_sem4_springboot_api.entities.Comment;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.repositories.ArticleRepository;
import com.example.project_sem4_springboot_api.repositories.CommentRepository;
import com.example.project_sem4_springboot_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;


    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Optional<Comment> getCommentById(Long id){
        return commentRepository.findById(id);
    }

    public Comment createComment(String content, Long createdId,Long articleId )throws IOException, ExecutionException, InterruptedException {
        User user = userRepository.findById(createdId).orElseThrow(() ->
                new RuntimeException("User not found"));
        Article article = articleRepository.findById(articleId).orElseThrow(() ->
                new RuntimeException("Article not found"));



        Comment comment = new Comment();
        comment.setArticle(article);
        comment.setContent(content);
        comment.setUser(user);
        comment.setCreatedAt(new Date());
        return commentRepository.save(comment);

    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
