package com.example.project_sem4_springboot_api.entities.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "Username is required")
//    @Size(min = 6, max = 50, message = "Username must be between 6 and 50 characters")
    private String username;
    @NotBlank(message = "Password is required")
//    @Size(min = 6, max = 50, message = "Password must be between 6 and 50 characters")
    private String password;

    private String deviceToken;
}
