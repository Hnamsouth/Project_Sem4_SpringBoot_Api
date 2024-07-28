package com.example.project_sem4_springboot_api.dto;

import com.example.project_sem4_springboot_api.entities.User;
import lombok.Builder;

@Builder
public class CommentDto {
    public Long id;
    public Long articleId;
    public User user;
    public String content;
    public String createdAt;
}
