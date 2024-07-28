package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.dto.CommentDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    @JsonBackReference
    private Article article;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;
    private String content;
    private Date createdAt;

    @JsonIgnore
    public CommentDto toRes(){
        return CommentDto.builder()
                .id(this.id)
                .articleId(this.article.getId())
                .user(this.user)
                .content(this.content)
                .createdAt(this.createdAt.toString())
                .build();
    }


}
