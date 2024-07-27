package com.example.project_sem4_springboot_api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonBackReference
    private Article article;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @Column(columnDefinition = "boolean default true")
    private boolean status;

    private Date createdAt;

    @JsonIgnore
    public Map<String,Object> toRes(){
        return Map.of(
                "id",this.id,
                "article",this.article.getId(),
                "userId",this.user.getId(),
                "status",this.status,
                "createdAt",this.createdAt
        );
    }


}
