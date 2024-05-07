package com.example.project_sem4_springboot_api.dto;

import com.example.project_sem4_springboot_api.entities.Teacher;
import com.example.project_sem4_springboot_api.entities.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserUpdateDto extends UserDetailDto{

    @NotNull(message = "Id không được để trống!")
    private Long id;



}
