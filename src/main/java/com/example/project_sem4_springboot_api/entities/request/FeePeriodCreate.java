package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.EStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class FeePeriodCreate {
    @NotBlank(message = "Tiêu đề không được để trống")
    public String title;
    public String conent;
    @NotNull(message = "Năm học không được để trống")
    public Long schoolYearId;

    @NotNull(message = "Danh sách phạm vi thu không được để trống")
    public FeePeriodScopeCreate feePeriodScope;

    @NotNull(message = "Danh sách Khoản thu không được để trống")
    public List<SchoolYearFeePeriodCreate> schoolYearFeePeriodCreateList;

    @JsonIgnore
    public FeePeriod toFeePeriod(SchoolYear schoolYear){
        return FeePeriod.builder()
                .title(title)
                .content(conent)
                .status(true)
                .statusCode(EStatus.HOAT_DONG.getName())
                .schoolyear(schoolYear)
                .createdAt(new java.util.Date())
                .build();
    }

    @JsonIgnore
    public List<FeePeriodScope> toFeePeriodScope(Scope scope, FeePeriod feePeriod){
        return feePeriodScope.getObjectIdList().stream().map(s->{
            return FeePeriodScope.builder()
                    .objectId(s)
                    .feePeriod(feePeriod)
                    .scope(scope)
                    .build();
        }).toList();
    }

    @JsonIgnore
    public List<SchoolYearFeePeriod> toSchoolYearFeePeriod(List<SchoolYearFee> schoolYearFees, FeePeriod feePeriod){
        return schoolYearFeePeriodCreateList.stream().map(s->{
            return SchoolYearFeePeriod.builder()
                    .amount(s.getAmount())
                    .feePeriod(feePeriod)
                    .schoolyearfee(schoolYearFees.stream().filter(fee -> fee.getId().equals(s.getSchoolYearFeeId())).findAny().get())
                    .build();
        }).toList();
    }
}
