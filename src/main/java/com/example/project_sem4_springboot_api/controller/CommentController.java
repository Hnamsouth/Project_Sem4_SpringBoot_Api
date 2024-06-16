package com.example.project_sem4_springboot_api.controller;

import com.example.project_sem4_springboot_api.entities.Comment;
import com.example.project_sem4_springboot_api.service.impl.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestParam Long articleId, @RequestParam Long userId, @RequestParam String content) {
        Comment comment = commentService.addComment(articleId, userId, content);
        return ResponseEntity.ok(comment);
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@RequestParam Long articleId) {
        List<Comment> comments = commentService.getCommentsByArticleId(articleId);
        return ResponseEntity.ok(comments);
    }
}
