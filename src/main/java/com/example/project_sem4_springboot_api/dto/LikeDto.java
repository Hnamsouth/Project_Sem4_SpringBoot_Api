package com.example.project_sem4_springboot_api.dto;

import com.example.project_sem4_springboot_api.entities.User;
import lombok.Builder;

import java.util.Date;

@Builder
public class LikeDto {
    public Long id;
    public Long articleId;
    public User user;
    public boolean status;
    public Date createdAt;
}
