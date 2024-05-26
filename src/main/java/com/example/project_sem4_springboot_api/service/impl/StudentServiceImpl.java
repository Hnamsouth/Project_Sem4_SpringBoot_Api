package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.EStatus;
import com.example.project_sem4_springboot_api.entities.request.AttendanceCreate;
import com.example.project_sem4_springboot_api.entities.response.StudentTransactionRes;
import com.example.project_sem4_springboot_api.exception.ArgumentNotValidException;
import com.example.project_sem4_springboot_api.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl  {

    private final String STUDENT_STATUS_CREATE= EStatus.STUDENT_DANG_HOC.name();
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final StudentStatusRepository studentStatusRepository;
    private final StatusRepository statusRepository;
    private final StudentYearInfoRepository studentYearInfoRepository;
    private final SchoolYearClassRepository schoolYearClassRepository;
    private final AttendanceRepository attendanceRepository;
    private final FeePeriodRepository feePeriodRepository;
    private final StudentTransactionRepository studentTransactionRepository;
    private final TransactionDetailRepository transactionDetailRepository;

    public ResponseEntity<?> createStudent(StudentDto data) {
        Date newDate = new Date(System.currentTimeMillis());
        var schoolYearClass = schoolYearClassRepository.findById(data.getSchoolYearClassId()).orElseThrow(
                ()->new NullPointerException("Không tìm thấy lớp học."));
        Student student = data.toStudent();
        var newStudent = studentRepository.save(student);
        var status = statusRepository.findByCode(STUDENT_STATUS_CREATE);
        // add student_status
        StudentStatus sts = StudentStatus.builder().student(newStudent).status(status)
        .createdAt(newDate).description("Bắt đầu nhập học.").build();
        studentStatusRepository.save(sts);
        // add student_year_info
        StudentYearInfo studentYearInfo = StudentYearInfo.builder()
                .students(newStudent).schoolYearClass(schoolYearClass).createdAt(newDate).build();
        studentYearInfoRepository.save(studentYearInfo);

        return ResponseEntity.ok(newStudent);
    }

    public ResponseEntity<?> getStudentInfoBy(
            Long bySchoolYearClassId,
            Long bySchoolYearId,
            Long byParentId,
            Long statusId,
            String byNameOrCode,
            Integer limit
    ) {
        if(byParentId!=null){
            var listStudent = userRepository.findById(byParentId).orElseThrow(()->new NullPointerException("Phụ huynh không tồn tại.!!!")).getStudents();
            if(listStudent.isEmpty()) throw new NullPointerException("Danh sách học sinh rỗng .!!!");
            // lấy thông tin hs của năm học mới nhất
            List<StudentYearInfo> rs = listStudent.stream().map(e-> {
                var a = studentYearInfoRepository.findByStudents_IdOrderByCreatedAtAsc(e.getId());
                a.setStudents(a.getStudents().toResInfo());
                return a;
            }).toList();
            return ResponseEntity.ok(rs);
        }
        if((bySchoolYearClassId!=null && bySchoolYearId!=null) || (bySchoolYearClassId==null && bySchoolYearId==null))  throw new ArgumentNotValidException("Yêu cầu 1 trong 2 tham số bySchoolYearClassId hoặc bySchoolYearId","","");
        var studentInfo = limit != null ?  studentYearInfoRepository.findAllBySchoolYearClass_IdOrSchoolYearClass_SchoolYear_Id(bySchoolYearClassId, bySchoolYearId,
                PageRequest.of(0, limit, Sort.by("createdAt").descending())).toList()
                : studentYearInfoRepository.findAllBySchoolYearClass_IdOrSchoolYearClass_SchoolYear_Id(bySchoolYearClassId, bySchoolYearId);
        if(studentInfo.isEmpty()) throw new NullPointerException("Không tìm thấy thông tin học sinh.");
        var rs = studentInfo.stream().map(e->{
            e.setStudents(e.getStudents().toResInfo());
            return e;
        }).toList();
        if(statusId!=null){
            rs = rs.stream().filter(e->e.getStudents().getStudentStatuses().get(0).getStatus().getId().equals(statusId)).toList();
        }
        if(byNameOrCode!=null){
            rs = rs.stream().filter(e->{
                var fullname = e.getStudents().getFirstName()+e.getStudents().getLastName() ;
                return fullname.toLowerCase().trim().contains(byNameOrCode.toLowerCase()) || e.getStudents().getStudentCode().equals(byNameOrCode);
            }).toList();
        }
        return ResponseEntity.ok(rs);
    }

    public ResponseEntity<?> updateStudentInfo(Student data) {
        if(!studentRepository.existsById(data.getId())) throw new NullPointerException("Không tìm thấy học sinh!!!");
        return ResponseEntity.ok(studentRepository.save(data));
    }

    public Attendance markAttendance(Long studentYearInfoId, boolean status, String note) {
        StudentYearInfo studentYearInfo = studentYearInfoRepository.findById(studentYearInfoId)
                .orElseThrow(() -> new RuntimeException("StudentYearInfo not found"));

        Attendance attendance = Attendance.builder()
                .studentYearInfo(studentYearInfo)
                .status(status)
                .note(note)
                .createdAt(new Date())
                .build();

        return attendanceRepository.save(attendance);
    }

//    public ResponseEntity<?> createAttendance( List<AttendanceCreate> data) {
//        var listStudentClass = studentYearInfoRepository.findAll();
//        //add attendance
//
//        return ResponseEntity.ok(data);
//    }

    public ResponseEntity<?> createStudentTransaction(Long feePeriodId){

    var feePeriod = feePeriodRepository.findById(feePeriodId).orElseThrow(()->new NullPointerException("Không tìm thấy khoản thu với Id: " + feePeriodId));
    var scope = feePeriod.getFeePeriodScopes().get(0).getScope();
    List<StudentTransaction> studentTransactionList = new ArrayList<>();
    switch (scope.getCode()){
        case GRADE -> {
            // tạo transaction cho từng học sinh trong khối
            var studentList = studentYearInfoRepository.findAllBySchoolYearClass_Grade_IdInAndSchoolYearClass_SchoolYear_Id(
                    feePeriod.getFeePeriodScopes().stream().map(FeePeriodScope::getObjectId).toList(), feePeriod.getSchoolyear().getId());
        }
        case CLASS -> {
            // tạo transaction cho từng học sinh trong lớp
            // lấy tất cả hs trong các lớp
            var studentList = studentYearInfoRepository.findAllBySchoolYearClass_IdIn(
                    feePeriod.getFeePeriodScopes().stream().map(FeePeriodScope::getObjectId).toList()
            );
            // tạo transaction cho từng học sinh
            var stdTransData = studentList.stream().map(s->StudentTransaction.builder()
                    .status(EStatus.STUDENT_TRANS_UNPAID.getName())
                    .statusCode(EStatus.STUDENT_TRANS_UNPAID)
                    .feePeriod(feePeriod)
                    .studentYearInfo(s)
                    .build()).toList();
            var listStuTrans = studentTransactionRepository.saveAll(stdTransData);
            // tạo transaction detail cho từng khoản thu
            List<TransactionDetail> StdTransDetails = new ArrayList<>();
            listStuTrans.forEach(st->{
                feePeriod.getSchoolYearFeePeriods().forEach(s->{
                    // lấy giá tiền của khoản thu theo khối của hs hoặc lấy giá tiền mặc định
                    var price = s.getSchoolyearfee().getFeePrices().stream()
                            .filter(f->f.getGradeId().equals(st.getStudentYearInfo().getSchoolYearClass().getGrade().getId()))
                            .findAny()
                            .orElseGet(()->s.getSchoolyearfee().getFeePrices().get(0)
                            );
                    StdTransDetails.add(TransactionDetail.builder()
                            .title(s.getSchoolyearfee().getTitle())
                            .price(price.getPrice())
                            .amount(s.getAmount())
                            .studentTransaction(st)
                            .build());
                });
            });
            transactionDetailRepository.saveAll(StdTransDetails);
        }
        case SCHOOL -> {
            // tạo transaction cho tất cả học sinh
            var studentList = studentYearInfoRepository.findAllBySchoolYearClass_SchoolYear_Id(
                    feePeriod.getSchoolyear().getId());
        }
    }
    var resCheck = studentTransactionRepository.findAllByFeePeriod_Id(feePeriodId);
    return ResponseEntity.ok(resCheck);
}
    public ResponseEntity<?> getStudentTransaction(Long feePeriodId,Long studentId){
        var stdTrans = studentTransactionRepository.findByFeePeriod_IdAndStudentYearInfo_Id(feePeriodId,studentId);
        return ResponseEntity.ok(stdTrans.toResponse());
    }
}