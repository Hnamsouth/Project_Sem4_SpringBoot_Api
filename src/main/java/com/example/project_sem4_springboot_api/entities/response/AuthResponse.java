package com.example.project_sem4_springboot_api.entities.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    @JsonProperty("token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
}
