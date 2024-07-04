package com.example.project_sem4_springboot_api.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Data
@Table(name = "activates")
public class Activate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @Column(columnDefinition = "TEXT")
    private  String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;

}
