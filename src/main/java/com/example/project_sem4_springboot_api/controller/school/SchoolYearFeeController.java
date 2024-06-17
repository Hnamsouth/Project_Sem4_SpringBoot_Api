package com.example.project_sem4_springboot_api.controller.school;

import com.example.project_sem4_springboot_api.entities.request.SchoolYearFeeCreate;
import com.example.project_sem4_springboot_api.service.impl.SchoolYearFeeServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/school-year-fee")
@RequiredArgsConstructor
public class SchoolYearFeeController {
    private final SchoolYearFeeServiceImpl schoolYearFeeService;

    @GetMapping("/getBy")
    public ResponseEntity<?> getSchoolYearFeeBy(@RequestParam @NotNull Long schoolYearId){
        return schoolYearFeeService.getSchoolYearFeeBy(schoolYearId);
    }

    @GetMapping("/getUnitScopePaymentMethod")
    public ResponseEntity<?> getUnitScopePaymentMethod(){
        return schoolYearFeeService.getUnitScopePaymentMethod();
    }

    @PostMapping("/create")
    public ResponseEntity<?> createSchoolYearFee(@Valid @RequestBody SchoolYearFeeCreate data){
        return schoolYearFeeService.createSchoolYearFee(data);
    }
}
