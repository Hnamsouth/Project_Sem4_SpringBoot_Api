package com.example.project_sem4_springboot_api.controller.school;

import com.example.project_sem4_springboot_api.entities.request.FeePeriodCreate;
import com.example.project_sem4_springboot_api.entities.request.SchoolYearFeeCreate;
import com.example.project_sem4_springboot_api.service.impl.FeePeriodServiceImpl;
import com.example.project_sem4_springboot_api.service.impl.SchoolYearFeeServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/fee-period")
@RequiredArgsConstructor
public class FeePeriodController {
    private final FeePeriodServiceImpl feePeriodService;

    @GetMapping("/getBy")
    public ResponseEntity<?> getFeePeriodBy(@RequestParam @NotNull Long schoolYearId){
        return feePeriodService.getFeePeriodBy(schoolYearId);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createFeePeriodFee(@Valid @RequestBody FeePeriodCreate data){
        return feePeriodService.createFeePeriod(data);
    }


}
