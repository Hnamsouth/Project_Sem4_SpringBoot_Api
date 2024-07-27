package com.example.project_sem4_springboot_api.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public class LikeDto {
    public Long id;
    public Long articleId;
    public Long userId;
    public boolean status;
    public Date createdAt;
}
