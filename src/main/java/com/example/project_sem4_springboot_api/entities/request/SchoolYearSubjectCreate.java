package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.entities.SchoolYear;
import com.example.project_sem4_springboot_api.entities.SchoolYearSubject;
import com.example.project_sem4_springboot_api.entities.Subject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
    private Long subjectId;
    private List<Long> subjectIds;
    @NotNull(message = "SchoolYear is required")
    @Min(value = 1, message = "SchoolYear must be greater than 0")
    private Long schoolYearId;
}
