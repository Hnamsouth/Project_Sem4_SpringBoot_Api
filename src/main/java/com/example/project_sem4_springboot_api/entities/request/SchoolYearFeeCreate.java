package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.ETerm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SchoolYearFeeCreate {
    @NotBlank(message = "Tiêu đề không được để trống")
    public String title;
    @NotNull(message = "Kỳ thu học phí không được để trống")
    public boolean term;
    public boolean compel;
    public boolean status;
    public boolean refund;
    @NotNull(message = "paymentTimeId không được để trống")
    public Long paymentTimeId;
    @NotNull(message = "Id Năm học không được để trống")
    public Long schoolYearId;
    @NotEmpty(message = "Danh sách giá không được rỗng")
    public List<FeePriceCreate> feePriceList;

    @JsonIgnore
    public SchoolYearFee toSchoolYearFee(PaymentTime paymentTime, SchoolYear schoolYear){
        return SchoolYearFee.builder()
                .title(title)
                .term(term?ETerm.NAM:ETerm.THANG)
                .termName(term?ETerm.NAM.getTerm() : ETerm.THANG.getTerm())
                .compel(compel)
                .status(status)
                .refund(refund)
                .paymentTime(paymentTime)
                .schoolyear(schoolYear)
                .build();
    }
    @JsonIgnore
    public List<FeePrice> toFeePrice(List<Unit> units, SchoolYearFee school){
        return this.getFeePriceList().stream().map(f ->
                FeePrice.builder()
                        .price(f.getPrice())
                        .gradeId(f.getGradeId())
                        .schoolyearfee(school)
                        .unit(units.stream().filter(u->u.getId().equals(f.unitId)).findAny().get())
                        .build()
        ).toList();
    }
}
