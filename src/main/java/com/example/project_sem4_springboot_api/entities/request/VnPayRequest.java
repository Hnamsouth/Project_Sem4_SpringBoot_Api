package com.example.project_sem4_springboot_api.entities.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VnPayRequest {
    public int total;
    public String orderInfo;
}
