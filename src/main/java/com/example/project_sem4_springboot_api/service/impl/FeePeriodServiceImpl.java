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
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;
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
    private final StudentYearInfoRepository studentYearInfoRepository;
    private final StudentTransactionRepository studentTransactionRepository;
    private final TransactionDetailRepository transactionDetailRepository;
    private final ExecutorService executorService;
    private final TransactionTemplate transactionTemplate;

    /**
     * @return listOf dot thu + thong ke lop hoc + sl hs cua lop trong pham vi dot thu da dong
     * */
    public ResponseEntity<?> getFeePeriodBy(Long schoolYearId, Long FeePeriodId) {
        List<FeePeriod> feePeriods =schoolYearId !=null? feePeriodRepository.findAllBySchoolyear_Id(schoolYearId): List.of(feePeriodRepository.findById(FeePeriodId).orElseThrow())  ;
        List<FeePeriodRes> res = new ArrayList<>();
        feePeriods.forEach(fp->{
            // check scope
            var scope = fp.getFeePeriodScopes().get(0);
            var objectIds = fp.getFeePeriodScopes().stream().map(FeePeriodScope::getObjectId).toList();
            boolean checkScope = scope.getScope().getCode().equals(EScope.GRADE);
            var classList = checkScope ? schoolYearClassRepository.findAllByGrade_IdIn(objectIds):
                    schoolYearClassRepository.findAllByIdIn(objectIds);
            if(classList.isEmpty()) throw new NullPointerException("Danh sách id "+(checkScope?"khoi":"lop")+" không hợp lệ");

            var stdTrans = fp.getStudentTransactions();
            List<Map<String, Object>> classListRs = new ArrayList<>(
                classList.stream().map(c -> {
                    Map<String, Object> classRs = new HashMap<>();
                    var stdTransOfClass = stdTrans.stream().filter(st ->st.getStudentYearInfo().getSchoolYearClass().equals(c)).toList();
                    classRs.put("schoolYearClass", c.toRes());
                    classRs.put("totalStudent", c.getStudentYearInfos().size());
                    classRs.put("totalPaid", stdTransOfClass.stream().filter(st -> st.getStatusCode().equals(EStatus.STUDENT_TRANS_PAID)).toList().size());
                    return classRs;
            }).toList());

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
        var schoolYear = schoolYearRepository.findById(data.getSchoolYearId())
                .orElseThrow(()->new NullPointerException("Không tìm thấy năm học với Id: " + data.getSchoolYearId()));
        // check fee period scope
        var scope = scopeRepository.findById(data.getFeePeriodScope().getScopeId())
                .orElseThrow(() -> new NullPointerException("Không tìm thấy phạm vi thu với Id: " + data.getFeePeriodScope().getScopeId()));
        boolean checkScope = scope.getCode().equals(EScope.GRADE);
        var classList = checkScope ?
                schoolYearClassRepository.findAllByGrade_IdIn(data.getFeePeriodScope().getObjectIdList()) :
                schoolYearClassRepository.findAllByIdIn(data.getFeePeriodScope().getObjectIdList());
        if(classList.isEmpty()) throw new NullPointerException("Danh sách id "+(checkScope?"khoi":"lop")+" không hợp lệ");
        // check school year fee
        var listSchoolYearFeePeriod = data.getSchoolYearFeePeriodCreateList().stream().map(SchoolYearFeePeriodCreate::getSchoolYearFeeId).toList();
        var listSchoolYearFee = schoolYearFeeRepository.findAllByIdIn(listSchoolYearFeePeriod);
        if(listSchoolYearFee.isEmpty()) throw new NullPointerException("Danh sách id khoản thu không hợp lệ");

        var feePeriod = feePeriodRepository.save(data.toFeePeriod(schoolYear));
        var feePeriodScope = feePeriodScopeRepository.saveAll(data.toFeePeriodScope(scope, feePeriod));
        var schoolYearFeePeriod = schoolYearFeePeriodRepository.saveAll(data.toSchoolYearFeePeriod(listSchoolYearFee, feePeriod));
        // create student transaction
        createStudentTrans(scope.getCode(),data,feePeriod,schoolYearFeePeriod);

        return ResponseEntity.ok("Tạo đợt thu thành công.");
    }

    private void createStudentTrans(EScope scope,FeePeriodCreate data,FeePeriod feePeriod,List<SchoolYearFeePeriod> schoolYearFeePeriod){
        createTransactions(getStudentByScope(scope,data,feePeriod),feePeriod,schoolYearFeePeriod);
    }
    private List<StudentYearInfo> getStudentByScope(EScope scope,FeePeriodCreate data,FeePeriod feePeriod){
        switch (scope){
            case GRADE -> {
                // tạo transaction cho từng học sinh trong khối
                return studentYearInfoRepository.findAllBySchoolYearClass_Grade_IdInAndSchoolYearClass_SchoolYear_Id(
                        data.getFeePeriodScope().getObjectIdList(), feePeriod.getSchoolyear().getId());
            }
            case CLASS -> {
                // tạo transaction cho từng học sinh trong lớp
                // lấy tất cả hs trong các lớp
                return studentYearInfoRepository.findAllBySchoolYearClass_IdIn(data.getFeePeriodScope().getObjectIdList());
            }
            default -> {return studentYearInfoRepository.findAllBySchoolYearClass_SchoolYear_Id(
                    feePeriod.getSchoolyear().getId());}
        }
    }
    private void createTransactions(List<StudentYearInfo> studentList,FeePeriod feePeriod,List<SchoolYearFeePeriod> schoolYearFeePeriod){

            var stdTransData = studentList.stream().map(s->StudentTransaction.builder()
                    .status(EStatus.STUDENT_TRANS_UNPAID.getName())
                    .statusCode(EStatus.STUDENT_TRANS_UNPAID)
                    .feePeriod(feePeriod)
                    .studentYearInfo(s)
                    .build()).toList();
            var listStuTrans = studentTransactionRepository.saveAll(stdTransData);
            // tạo transaction detail cho từng khoản thu
            List<TransactionDetail> StdTransDetails = new ArrayList<>();

            var stdTrans =  listStuTrans.stream().map(st->{
                // nếu dùng giá trị  là kiểu dữ liệu nguyên thủy thì phải dùng AtomicReference vì giá trị bên trong không thể thay đổi
                // nên không thể thay đổi giá trị của biến total
                // phải cấp phát bộ nhớ mới cho biến total bằng cách dùng AtomicReference
                AtomicReference<Double> total = new AtomicReference<>(0.0);
                schoolYearFeePeriod.forEach(s->{
                    // lấy giá tiền của khoản thu theo khối của hs hoặc lấy giá tiền mặc định
                    var price = s.getSchoolyearfee().getFeePrices().stream()
                            .filter(f->f.getGradeId() !=null && f.getGradeId().equals(st.getStudentYearInfo().getSchoolYearClass().getGrade().getId()))
                            .findAny()
                            .orElseGet(()->s.getSchoolyearfee().getFeePrices().get(0)
                            );
                    StdTransDetails.add(TransactionDetail.builder()
                            .title(s.getSchoolyearfee().getTitle())
                            .price(price.getPrice())
                            .amount(s.getAmount())
                            .studentTransaction(st)
                            .build());
                    total.updateAndGet(v -> (v + price.getPrice() * s.getAmount()));
                });
                st.setTotal(total.get());
                return st;
            }).toList();
            assert(stdTrans.size() == listStuTrans.size());
            studentTransactionRepository.saveAll(stdTrans);
            transactionDetailRepository.saveAll(StdTransDetails);
    }

}

