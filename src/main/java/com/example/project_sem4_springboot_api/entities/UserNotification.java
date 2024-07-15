package com.example.project_sem4_springboot_api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notification")
public class UserNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String title;
    private String content;
    private String action;
    private String router;
    private String image;
    private LocalDate createdAt;
    private boolean status;
    private boolean hasRead;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;


    @JsonIgnore
    public Map<String,String> toData(){
        return Map.of(
                "id",String.valueOf(id),
                "title",title,
                "content",content,
                "action",action,
                "router",router,
                "image",image,
                "createdAt",createdAt.toString(),
                "status",String.valueOf(status)
        );
    }

}
