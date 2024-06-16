package com.example.project_sem4_springboot_api.controller;

import com.example.project_sem4_springboot_api.service.impl.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> likeArticle(@RequestParam Long articleId, @RequestParam Long userId) {
        likeService.likeArticle(articleId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countLikes(@RequestParam Long articleId) {
        long likeCount = likeService.countLikes(articleId);
        return ResponseEntity.ok(likeCount);
    }
}
