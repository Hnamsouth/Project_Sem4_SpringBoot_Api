package com.example.project_sem4_springboot_api.entities.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Size(min = 8,max=255)
    private String username;

    @NotBlank
    @Size(min = 8,max=255)
    private String password;

    @NotNull
    private Set<Long> role;
    @NotBlank
    private  String first_name;
    @NotBlank
    private  String last_name;
    @NotBlank
    private  String address;
    @NotBlank @Size(min=9,max=20)
    private  String phone;
    @NotBlank
    private  String email;
    @NotBlank
    private  boolean gender;
    private  Date birthday;
    private  String citizen_id;
    private  String nation;
    private  String avatar;

}
