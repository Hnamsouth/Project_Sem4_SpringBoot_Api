package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.EScope;
import com.example.project_sem4_springboot_api.entities.enums.EStatus;
import com.example.project_sem4_springboot_api.entities.request.FeePeriodCreate;
import com.example.project_sem4_springboot_api.entities.request.FeePeriodScopeCreate;
import com.example.project_sem4_springboot_api.entities.request.SchoolYearFeePeriodCreate;
import com.example.project_sem4_springboot_api.entities.response.FeePeriodRes;
import com.example.project_sem4_springboot_api.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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


    /**
     * @return listOf dot thu + thong ke lop hoc + sl hs cua lop trong pham vi dot thu da dong
     * */
    public ResponseEntity<?> getFeePeriodBy(Long schoolYearId) {
        var feePeriods = feePeriodRepository.findAllBySchoolyear_Id(schoolYearId);
        List<FeePeriodRes> res = new ArrayList<>();
        feePeriods.forEach(fp->{
            // check scope
            var scope = fp.getFeePeriodScopes().get(0);
            var scopeIds = fp.getFeePeriodScopes().stream().map(FeePeriodScope::getId).toList();
            boolean checkScope = scope.getScope().getCode().equals(EScope.GRADE);
            var classList = checkScope ? schoolYearClassRepository.findAllByGrade_IdIn(scopeIds):
                    schoolYearClassRepository.findAllByIdIn(scopeIds);
            if(classList.isEmpty()) throw new NullPointerException("Danh sách id "+(checkScope?"khoi":"lop")+" không hợp lệ");

            var stdTrans = fp.getStudentTransactions();
            List<Map<String,Object>> classListRs = new ArrayList<>();
            classList.forEach(c->{
                Map<String,Object> classRs = new HashMap<>();
                var stdTransOfClass = stdTrans.stream().filter(st->st.getStudentYearInfo().getSchoolYearClass().getId().equals(c.getId())).toList();
                classRs.put("schoolYearClass",c);
                classRs.put("totalStudent",stdTransOfClass.size());
                classRs.put("totalPaid",stdTransOfClass.stream().filter(st->st.getStatusCode().equals(EStatus.STUDENT_TRANS_PAID)).toList().size());
                classListRs.add(classRs);
            });
            res.add(FeePeriodRes.builder()
                    .id(fp.getId())
                    .title(fp.getTitle())
                    .content(fp.getContent())
                    .status(fp.isStatus())
                    .statusCode(fp.isStatus()?EStatus.HOAT_DONG.getName():EStatus.NGUNG_HOAT_DONG.getName())
                    .endDate(fp.getEndDate().toString())
                    .createdAt(fp.getCreatedAt().toString())
                    .classList(classListRs)
                    .totalTrans(stdTrans.size())
                    .totalPaid(stdTrans.stream().filter(st->st.getStatusCode().equals(EStatus.STUDENT_TRANS_PAID)).toList().size())
                    .build());
            // check student transaction status
        });
        // check student transaction
        return ResponseEntity.ok(res);
    }

    public ResponseEntity<?> createFeePeriod(FeePeriodCreate data) {
        var schoolYear = schoolYearRepository.findById(data.getSchoolYearId()).orElseThrow(()->new NullPointerException("Không tìm thấy năm học với Id: " + data.getSchoolYearId()));

        // check fee period scope
        var scope = scopeRepository.findById(data.getFeePeriodScope().getScopeId()).orElseThrow(() -> new NullPointerException("Không tìm thấy phạm vi thu với Id: " + data.getFeePeriodScope().getScopeId()));
        switch (scope.getCode()){
            case GRADE -> {
                var gradeList = gradeRepository.findAllByIdIn(data.getFeePeriodScope().getObjectIdList());
                if(gradeList.isEmpty()) throw new NullPointerException("Danh sách id khối không hợp lệ");
            }
            case CLASS -> {
               var classList = schoolYearClassRepository.findAllByIdIn(data.getFeePeriodScope().getObjectIdList());
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

