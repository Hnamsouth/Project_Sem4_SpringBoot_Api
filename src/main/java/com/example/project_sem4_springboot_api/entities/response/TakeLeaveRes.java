package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.TakeLeave;
import com.example.project_sem4_springboot_api.entities.enums.HandleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
public class TakeLeaveRes{
    private Long id;
    private String note;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate createdAt;
    @Enumerated(EnumType.STRING)
    private HandleStatus status;
    private String statusName;
    private StudentResponse studentInfo;

    @JsonIgnore
    static public TakeLeaveRes toResParent(TakeLeave data ){
        return TakeLeaveRes.builder()
                .id(data.getId())
                .note(data.getNote())
                .startDate(data.getStartDate())
                .endDate(data.getEndDate())
                .createdAt(data.getCreatedAt())
                .status(data.getStatus())
                .statusName(data.getStatusName())
                .studentInfo(data.getStudentYearInfo().toStudentResponse())
                .build();
    }
}
