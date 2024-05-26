package com.example.project_sem4_springboot_api.entities.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeePriceCreate {
    public double price;
    public Long gradeId;
    public Long unitId;
}
