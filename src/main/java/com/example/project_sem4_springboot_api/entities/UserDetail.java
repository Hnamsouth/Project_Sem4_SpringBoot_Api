package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.dto.TeacherUpdateDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonBackReference
    private User user;

    @JsonIgnore
    public UserDetail getDto(@Nullable boolean getU){
        return UserDetail.builder()
            .id(id).firstname(firstname).lastname(lastname).address(address).phone(phone).email(email).gender(gender)
            .birthday(birthday).citizen_id(citizen_id).nation(nation).avatar(avatar).user(getU ? user:null).build();
    }

    @JsonIgnore
    public UserDetail from(TeacherUpdateDto data){
        this.firstname = data.getFirst_name();
        this.lastname = data.getLast_name();
        this.address = data.getAddress();
        this.phone = data.getPhone();
        this.email = data.getEmail();
        this.nation= data.getNation();
        this.gender = data.isGender();
        this.birthday = data.getBirthday();
        this.citizen_id = data.getCitizen_id();
        this.avatar = data.getAvatar();
        return this;
    }
}
