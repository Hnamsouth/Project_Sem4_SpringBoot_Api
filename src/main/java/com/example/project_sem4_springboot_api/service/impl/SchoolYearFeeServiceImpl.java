package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.entities.FeePrice;
import com.example.project_sem4_springboot_api.entities.request.SchoolYearFeeCreate;
import com.example.project_sem4_springboot_api.entities.response.PreDataFeeCreate;
import com.example.project_sem4_springboot_api.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SchoolYearFeeServiceImpl {

    private final SchoolYearRepository schoolYearRepository;
    private final SchoolYearFeeRepository schoolYearFeeRepository;
    private final PaymentTimeRepository paymentTimeRepository;
    private final UnitRepository unitRepository;
    private final ScopeRepository scopeRepository;
    private final FeePriceRepository feePriceRepository;

    public ResponseEntity<?> getSchoolYearFeeBy(Long schoolYearId) {
        var res = schoolYearFeeRepository.findAllBySchoolyear_Id(schoolYearId);
        return ResponseEntity.ok(res);
    }
    public ResponseEntity<?> createSchoolYearFee(SchoolYearFeeCreate data){
        // check payment time and school year
        var paymentTime = paymentTimeRepository.findById(data.getPaymentTimeId()).orElseThrow(() -> new NullPointerException("Không tìm thấy Thời gian thanh toán với Id: " + data.getPaymentTimeId()));
        var schoolYear = schoolYearRepository.findById(data.getSchoolYearId()).orElseThrow(() -> new NullPointerException("Không tìm thấy Năm học với Id: " + data.getSchoolYearId()));
        // check unit
        var listUnitId = data.getFeePriceList().stream().map(feePriceCreate ->feePriceCreate.unitId).toList();
        var listUnit = unitRepository.findAllByIdIn(listUnitId);
        if(listUnit.isEmpty()) throw new NullPointerException("Không tìm thấy đơn vị với unitId list: " + listUnitId);
        // create school year fee
        var schoolYearFee= schoolYearFeeRepository.save(data.toSchoolYearFee(paymentTime, schoolYear));
        var feePrice = feePriceRepository.saveAll(data.toFeePrice(listUnit, schoolYearFee));

        return ResponseEntity.ok(schoolYearFeeRepository.findById(schoolYearFee.getId()));
    }
    public  ResponseEntity<?> getUnitScopePaymentMethod(){
        var res = PreDataFeeCreate.builder()
                .paymentTimeList(paymentTimeRepository.findAll())
                .scopeList(scopeRepository.findAll())
                .unitList(unitRepository.findAll())
                .build();

        return ResponseEntity.ok(res);
    }
}
