package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.dto.LikeDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "likes",uniqueConstraints ={@UniqueConstraint(columnNames = {"article_id", "user_id"})})
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    @JsonManagedReference
    private Article article;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference
    private User user;
    @Column(columnDefinition = "boolean default true")
    private boolean status;
    private Date createdAt;

    @JsonIgnore
    public LikeDto toRes(){
        return LikeDto.builder()
                .id(this.id)
                .articleId(this.article.getId())
                .userId(this.user.getId())
                .status(this.status)
                .createdAt(this.createdAt)
                .build();
    }


}
