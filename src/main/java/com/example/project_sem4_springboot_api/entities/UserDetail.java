package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.annotation.Nullable;
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
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;

    public UserDetail getDto(@Nullable  boolean getU){
        return UserDetail.builder()
                .id(this.id)
                .firstname(this.firstname)
                .lastname(this.lastname)
                .address(this.address)
                .phone(this.phone)
                .email(this.email)
                .gender(this.gender)
                .birthday(this.birthday)
                .citizen_id(this.citizen_id)
                .nation(this.nation)
                .avatar(this.avatar)
                .user(getU ? this.user:null)
                .build();
    }
}
