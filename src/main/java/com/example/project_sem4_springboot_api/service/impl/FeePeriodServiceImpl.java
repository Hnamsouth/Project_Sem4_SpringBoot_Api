package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.EStatus;
import com.example.project_sem4_springboot_api.entities.request.FeePeriodCreate;
import com.example.project_sem4_springboot_api.entities.request.FeePeriodScopeCreate;
import com.example.project_sem4_springboot_api.entities.request.SchoolYearFeePeriodCreate;
import com.example.project_sem4_springboot_api.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeePeriodServiceImpl {

    private final FeePeriodRepository feePeriodRepository;
    private final ScopeRepository scopeRepository;
    private final FeePeriodScopeRepository feePeriodScopeRepository;
    private final SchoolYearRepository schoolYearRepository;
    private final SchoolYearFeePeriodRepository schoolYearFeePeriodRepository;
    private final SchoolYearFeeRepository schoolYearFeeRepository;
    private final SchoolYearClassRepository schoolYearClassRepository;
    private final GradeRepository gradeRepository;

    public ResponseEntity<?> getFeePeriodBy(Long schoolYearId) {
        var res = feePeriodRepository.findAllBySchoolyear_Id(schoolYearId);
        return ResponseEntity.ok(res);
    }

    public ResponseEntity<?> createFeePeriod(FeePeriodCreate data) {
        var schoolYear = schoolYearRepository.findById(data.getSchoolYearId()).orElseThrow(()->new NullPointerException("Không tìm thấy năm học với Id: " + data.getSchoolYearId()));

        // check fee period scope
        var scope = scopeRepository.findById(data.getFeePeriodScope().getScopeId()).orElseThrow(() -> new NullPointerException("Không tìm thấy phạm vi thu với Id: " + data.getFeePeriodScope().getScopeId()));
        switch (scope.getCode()){
            case GRADE -> {
                var gradeList = gradeRepository.findByIdIn(data.getFeePeriodScope().getObjectIdList());
                if(gradeList.isEmpty()) throw new NullPointerException("Danh sách id khối không hợp lệ");
            }
            case CLASS -> {
               var classList = schoolYearClassRepository.findByIdIn(data.getFeePeriodScope().getObjectIdList());
                if(classList.isEmpty()) throw new NullPointerException("Danh sách id lớp không hợp lệ");
            }
        }
        // check school year fee
        var listSchoolYearFeePeriod = data.getSchoolYearFeePeriodCreateList().stream().map(SchoolYearFeePeriodCreate::getSchoolYearFeeId).toList();
        var listSchoolYearFee = schoolYearFeeRepository.findAllByIdIn(listSchoolYearFeePeriod);
        if(listSchoolYearFee.isEmpty()) throw new NullPointerException("Danh sách id khoản thu không hợp lệ");

        var feePeriod = feePeriodRepository.save(data.toFeePeriod(schoolYear));
        var feePeriodScope = feePeriodScopeRepository.saveAll(data.toFeePeriodScope(scope, feePeriod));
        var schoolYearFeePeriod = schoolYearFeePeriodRepository.saveAll(data.toSchoolYearFeePeriod(listSchoolYearFee, feePeriod));
        // create student transaction

        return ResponseEntity.ok(feePeriodRepository.findById(feePeriod.getId()));
    }

}

