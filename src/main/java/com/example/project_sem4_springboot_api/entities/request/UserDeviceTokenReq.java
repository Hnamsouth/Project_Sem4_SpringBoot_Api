package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.entities.enums.EDeviceType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDeviceTokenReq {
    @NotBlank
    private String deviceToken;
    @NotBlank
    @Enumerated(EnumType.STRING)
    private EDeviceType deviceType;
}
