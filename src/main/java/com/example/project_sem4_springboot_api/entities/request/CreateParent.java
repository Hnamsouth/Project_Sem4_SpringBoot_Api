package com.example.project_sem4_springboot_api.entities.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CreateParent {
    private Long id;
    @NotBlank(message = "Full name is required")
    private String fullName;
    @NotBlank(message = "Phone is required")
    @Size(min = 10, max = 13, message = "Phone must be 10 or 13 characters")
    private String phone;
    @NotBlank (message = "Address is required")
    private String address;
    @NotNull(message = "Gender is required")
    private boolean gender;
    @NotBlank (message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;
    private List<Long> students;

}
