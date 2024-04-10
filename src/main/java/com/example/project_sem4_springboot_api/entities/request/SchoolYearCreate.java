package com.example.project_sem4_springboot_api.entities.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
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
public class SchoolYearCreate {
    @NotNull(message = "StartSem1 is required")
    @FutureOrPresent(message = "StartSem1 must be in the future or present")
    private Date startSem1;
    @NotNull(message = "StartSem2 is required")
    @Future(message = "StartSem2 must be in the future")
    private Date startSem2;
    @NotNull(message = "End is required")
    @Future(message = "End must be in the future")
    private Date end;
}
