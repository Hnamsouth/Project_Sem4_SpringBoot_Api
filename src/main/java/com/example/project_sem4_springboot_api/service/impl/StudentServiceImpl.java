package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.EStatus;
import com.example.project_sem4_springboot_api.entities.enums.HandleStatus;
import com.example.project_sem4_springboot_api.entities.enums.PaymentMethod;
import com.example.project_sem4_springboot_api.entities.request.AttendanceBody;
import com.example.project_sem4_springboot_api.entities.request.AttendanceCreateOrUpdate;
import com.example.project_sem4_springboot_api.entities.request.TakeLeaveRequest;
import com.example.project_sem4_springboot_api.entities.response.TakeLeaveRes;
import com.example.project_sem4_springboot_api.exception.ArgumentNotValidException;
import com.example.project_sem4_springboot_api.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
    private final TakeLeaveRepository takeLeaveRepository;

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

    public ResponseEntity<?> markAttendance(AttendanceCreateOrUpdate data) {
        var classInfo = schoolYearClassRepository.findById(data.getClassId()).orElseThrow(()-> new NullPointerException("Không tìm thấy thông tin lớp học."));
        var students = studentYearInfoRepository.findAllByIdIn(data.getListStudent().stream().map(AttendanceBody::getStudentYearInfoId).toList());

        if(students.isEmpty()) throw new NullPointerException("Danh sách học sinh không hợp lệ.");
        if(students.stream().anyMatch(e->!e.getSchoolYearClass().equals(classInfo))) throw new ArgumentNotValidException("Danh sách học sinh không thuộc lớp học này.","","");

        // get day of week from date

        var dayOfWeek =  Calendar.getInstance();
        dayOfWeek.setTime(data.dayOff);
        var dow = dayOfWeek.get(Calendar.DAY_OF_WEEK);
        if(dow == Calendar.SUNDAY || dow == Calendar.SATURDAY) throw new ArgumentNotValidException("Không thể điểm danh vào ngày nghỉ.","","");

        var attendanceList = new LinkedList<Attendance>();
        data.getListStudent().forEach(st->{
            attendanceList.add(Attendance.builder()
                     .id(st.getId())
                    .studentYearInfo(students.stream().filter(e->e.getId().equals(st.getStudentYearInfoId())).findAny().orElseThrow())
                    .attendanceStatus(st.getStatus())
                    .attendanceStatusName(st.getStatus().getName())
                    .notificationStatus(EStatus.CHUA_THONG_BAO.getName())
                    .createdAt(data.dayOff)
                    .note(st.getNote())
                    .build());
        });
        var res =attendanceRepository.saveAll(attendanceList);
        return ResponseEntity.ok(res);
    }
    public ResponseEntity<?> getAttendanceBy(Long studentYearInfoId,Date date,Long schoolYearClassId){
        if(studentYearInfoId==null && schoolYearClassId==null) throw new ArgumentNotValidException("Yêu cầu tham số studentYearInfoId hoặc schoolYearClassId","","");
        if(schoolYearClassId!=null){
            var attendance = attendanceRepository.getAttendanceClassWithDayOff(schoolYearClassId,date);
            return ResponseEntity.ok(attendance.stream().map(Attendance::toRes).toList());
        }
        var attendance = attendanceRepository.findAllByStudentYearInfo_Id(studentYearInfoId);
        return ResponseEntity.ok(attendance.stream().map(Attendance::toRes).toList());
    }

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

            var stdTrans =  listStuTrans.stream().map(st->{
                // nếu dùng giá trị  là kiểu dữ liệu nguyên thủy thì phải dùng AtomicReference vì giá trị bên trong không thể thay đổi
                // nên không thể thay đổi giá trị của biến total
                // phải cấp phát bộ nhớ mới cho biến total bằng cách dùng AtomicReference
                AtomicReference<Double> total = new AtomicReference<>(0.0);
                feePeriod.getSchoolYearFeePeriods().forEach(s->{
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
        if(feePeriodId!=null){
            var stdTrans = studentTransactionRepository.findByFeePeriod_IdAndStudentYearInfo_Id(feePeriodId,studentId);
            return ResponseEntity.ok(stdTrans.toResponse());
        }
        var stdTrans = studentTransactionRepository.findAllByStudentYearInfo_Id(studentId);
        if(stdTrans.isEmpty()) throw new NullPointerException("Không tìm thấy thông tin giao dịch của học sinh.");
        return ResponseEntity.ok(stdTrans.stream().map(StudentTransaction::toResponse).toList());
    }
    public ResponseEntity<?> transferSuccess(Long studentTransactionId , String transactionCode){
        var stdTrans = studentTransactionRepository.findById(studentTransactionId).orElseThrow(()->new NullPointerException("Không tìm thấy giao dịch."));
        // check giao dịch đã thanh toán chưa
        stdTrans.setPaid(stdTrans.getTotal());
        stdTrans.setStatus(EStatus.STUDENT_TRANS_PAID.getName());
        stdTrans.setStatusCode(EStatus.STUDENT_TRANS_PAID);
        stdTrans.setPaymentMethod(PaymentMethod.CHUYEN_KHOAN);

        studentTransactionRepository.save(stdTrans);
        return ResponseEntity.ok(stdTrans.toResponse());
    }

    public ResponseEntity<?> takeLeave(TakeLeaveRequest data){
        // khi phu huynh xin nghi hoc cho hs thi se tao ra danh sach nghi hoc cho hs va luu lai don xin nghỉ
        // va gui thong bao cho phu huynh
        var student = studentYearInfoRepository.findById(data.getStudentYearInfoId()).orElseThrow(()->new NullPointerException("Không tìm thấy thông tin học sinh."));
        var parent = student.getStudents().getParents().stream().filter(e->e.getId().equals(data.getUserId())).findAny().orElseThrow(()->new NullPointerException("Không tìm thấy thông tin người gửi."));
        var currentDate = LocalDate.now();
        // check ngày bắt đầu và ngày kết thúc
        if(data.getStartDate().compareTo(currentDate) < 0 || data.getEndDate().compareTo(currentDate) < 0){
            throw new ArgumentNotValidException("Ngày bắt đầu và ngày kết thúc không hợp lệ","","");
        }
        var takeLeave = TakeLeave.builder()
                .startDate(data.getStartDate())
                .endDate(data.getEndDate())
                .status(HandleStatus.CHO_XAC_NHAN)
                .statusName(HandleStatus.CHO_XAC_NHAN.getName())
                .note(data.getNote())
                .createdAt(currentDate)
                .studentYearInfo(student)
                .parent(parent)
                .build();
        var res = takeLeaveRepository.save(takeLeave);

//        var days= Period.between(data.getStartDate(),data.getEndDate()).getDays();
//        if(days > 0){
//            List<Attendance> attendanceList = new LinkedList<>();
//            for(int i=0;i<=days;i++) {
//                var attendance = Attendance.builder()
//                        .studentYearInfo(student)
//                        .attendanceStatus(AttendanceStatus.NGHI_CO_PHEP)
//                        .attendanceStatusName(AttendanceStatus.NGHI_CO_PHEP.getName())
//                        .notificationStatus(EStatus.DA_THONG_BAO.getName())
//                        .note(data.getNote())
//                        .createdAt(localDateToDate(data.getStartDate().plusDays(i)))
//                        .build();
//                attendanceList.add(attendance);
//            }
//            var ListAttendance = attendanceRepository.saveAll(attendanceList);
//        }
//        var attendance = Attendance.builder()
//                .studentYearInfo(student)
//                .attendanceStatus(AttendanceStatus.NGHI_CO_PHEP)
//                .attendanceStatusName(AttendanceStatus.NGHI_CO_PHEP.getName())
//                .notificationStatus(EStatus.DA_THONG_BAO.getName())
//                .note(data.getNote())
//                .createdAt(localDateToDate(data.getStartDate()))
//                .build();
//        var Attendance = attendanceRepository.save(attendance);
        // gửi thông báo cho giáo viên chủ nhiệm

        return ResponseEntity.ok(res);
    }

    public ResponseEntity<?> getTakeLeave(Long parentId,Long studentId){
        return ResponseEntity.ok(takeLeaveRepository.findAllByParent_IdOrStudentYearInfo_Id(parentId,studentId).stream().map(TakeLeaveRes::toResParent).toList());
    }


    private Date localDateToDate(LocalDate date){
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}