package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.entities.SchoolYear;
import com.example.project_sem4_springboot_api.entities.Subject;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchoolYearSubjectCreate {
    private Long subject;
    private List<Long> subjects;
    @NotNull(message = "SchoolYear is required")
    @Min(value = 1, message = "SchoolYear must be greater than 0")
    private Long schoolYear;
}
