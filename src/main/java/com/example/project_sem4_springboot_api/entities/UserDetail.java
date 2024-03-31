package com.example.project_sem4_springboot_api.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_detail")
public class UserDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private String address;
    private String phone;
    private String email;
    private boolean gender;
    private Date birthday;
    private String citizen_id;
    private String nation;
    private String avatar;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


}
