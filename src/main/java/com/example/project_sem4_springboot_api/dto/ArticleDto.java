package com.example.project_sem4_springboot_api.dto;

import com.example.project_sem4_springboot_api.entities.Comment;
import com.example.project_sem4_springboot_api.entities.Like;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
@Data
@Builder
public class ArticleDto {
    private Long id;
    private String title;
    private String content;
    private String url;
    private List<String> articleImageUrls;
    private Date createdAt;
    private List<Like> likeList;
    private List<Comment> commentList;

}
