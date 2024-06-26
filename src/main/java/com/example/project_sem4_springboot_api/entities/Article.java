package com.example.project_sem4_springboot_api.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String content;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<Image> images;


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
        res.put("title",this.getTitle());
        res.put("content",this.getContent());
        res.put("images",this.getImages());
        res.put("userInfo",this.getUser().getUserDetail().get(0).getDto(false));
        return res;
    }
}
