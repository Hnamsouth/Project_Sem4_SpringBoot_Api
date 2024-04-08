package com.example.project_sem4_springboot_api.entities.request;


import com.example.project_sem4_springboot_api.entities.Student;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CreateParent {

    private Long id;
    @NotNull
    private String fullName;
    @NotNull
    private String phone;
    @NotNull
    private String address;
    @NotNull
    private boolean gender;
    @NotNull
    private String email;
    private List<Long> students;

}
