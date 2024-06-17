package com.example.project_sem4_springboot_api.entities.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class FeePeriodScopeCreate {
    @NotEmpty(message = "objectIdList không được để rỗng")
    public List<Long> objectIdList;
    @NotNull(message = "Id Phạm vi thu không được để trống")
    public Long scopeId;
}
