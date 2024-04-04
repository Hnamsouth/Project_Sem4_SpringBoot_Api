package com.example.project_sem4_springboot_api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolYearDto {
    @NotNull
    private Date startSem1;
    @NotNull
    private Date startSem2;
    @NotNull
    private Date end;
}
