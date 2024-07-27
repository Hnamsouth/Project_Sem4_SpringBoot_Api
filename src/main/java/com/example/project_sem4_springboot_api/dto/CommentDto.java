package com.example.project_sem4_springboot_api.dto;

import lombok.Builder;

@Builder
public class CommentDto {
    public Long id;
    public Long articleId;
    public Long userId;
    public String content;
    public String createdAt;
}
