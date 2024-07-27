package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.dto.ArticleDto;
import com.example.project_sem4_springboot_api.entities.request.HomeWorkDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String url;
    private Date createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Like> likes;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Comment> comments;

    public Article(Long id) {
        this.id =id;
    }

    @JsonIgnore
    public Map<String,Object>  toRes(){
        Map<String,Object> res = new HashMap<>();
        res.put("id",this.id);
        res.put("title",this.title);
        res.put("content",this.content);
        res.put("url",this.url);
        res.put("createdAt",this.createdAt);
        res.put("likeList",this.likes.stream().map(Like::toRes).toList());
        res.put("commentList",this.comments.stream().map(Comment::toRes).toList());
        return res;
    }


    @JsonIgnore
    public ArticleDto convertToDto() {
        System.out.println("likeList: " + this.likes.size()+"\t commentList: " + this.comments.size());

        var likeList = this.likes.stream().map(Like::toRes).toList();
        var commentList = this.comments.stream().map(Comment::toRes).toList();
        return ArticleDto.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .url(this.url)
                .createdAt(this.createdAt)
                .likeList(likeList)
                .commentList(commentList)
                .build();
    }




}
