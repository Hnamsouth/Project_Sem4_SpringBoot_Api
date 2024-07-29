package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.EGrade;
import com.example.project_sem4_springboot_api.entities.enums.EStatus;
import com.example.project_sem4_springboot_api.entities.enums.HandleStatus;
import com.example.project_sem4_springboot_api.entities.enums.PaymentMethod;
import com.example.project_sem4_springboot_api.entities.request.AttendanceBody;
import com.example.project_sem4_springboot_api.entities.request.AttendanceCreateOrUpdate;
import com.example.project_sem4_springboot_api.entities.request.TakeLeaveRequest;
import com.example.project_sem4_springboot_api.entities.request.UserNotifyRes;
import com.example.project_sem4_springboot_api.entities.response.ResultPaginationDto;
import com.example.project_sem4_springboot_api.entities.response.TakeLeaveRes;
import com.example.project_sem4_springboot_api.exception.ArgumentNotValidException;
import com.example.project_sem4_springboot_api.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    private final UserNotificationRepository userNotificationRepository;
    private final StudentStatusRepository studentStatusRepository;
    private final StatusRepository statusRepository;
    private final StudentYearInfoRepository studentYearInfoRepository;
    private final StudentScoreSubjectRepository studentScoreSubjectRepository;
    private final SchoolYearClassRepository schoolYearClassRepository;
    private final AttendanceRepository attendanceRepository;
    private final FCMService fcmService;
    private final StudentTransactionRepository studentTransactionRepository;
    private final TransactionDetailRepository transactionDetailRepository;
    private final TakeLeaveRepository takeLeaveRepository;

    public ResponseEntity<?> createStudent(StudentDto data) {
        Date newDate = new Date(System.currentTimeMillis());
        var schoolYearClass = schoolYearClassRepository.findById(data.getSchoolYearClassId()).orElseThrow(
                ()->new NullPointerException("Không tìm thấy lớp học."));
//        var checkGrade = schoolYearClass.getGrade().getName().equals(EGrade.KHOI_1);
//        if(!checkGrade  ) throw new ArgumentNotValidException("Chỉ có thể thêm mới học sinh vào khối 1","","");
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
        var stdYIF = studentYearInfoRepository.save(studentYearInfo);
        // add student_score_subject
        List<StudentScoreSubject> stdScore = new ArrayList<>();
        schoolYearClass.getTeacherSchoolYearClassSubjects().forEach(t->
                stdScore.add(StudentScoreSubject.builder()
                        .studentYearInfo(stdYIF)
                        .schoolYearSubject(t.getSchoolYearSubject())
                        .teacherSchoolYear(t.getTeacherSchoolYear())
                        .createdAt(newDate)
                        .build()));
        studentScoreSubjectRepository.saveAll(stdScore);
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

    public ResultPaginationDto getAllStudent(Specification<Student> specification, Pageable pageable){
        Page<Student> studentPage = studentRepository.findAll(specification, pageable);
        ResultPaginationDto rs = new ResultPaginationDto();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(studentPage.getTotalPages());
        meta.setTotal(studentPage.getTotalElements());

        rs.setMeta(meta);
        rs.setResult(studentPage.getContent());
        return rs;
    }

    public ResponseEntity<?> updateStudentInfo(Student data) {
        if(!studentRepository.existsById(data.getId())) throw new NullPointerException("Không tìm thấy học sinh!!!");
        return ResponseEntity.ok(studentRepository.save(data));
    }

    public ResponseEntity<?> markAttendance(AttendanceCreateOrUpdate data) {
        var stdIds = data.getListStudent().stream().map(AttendanceBody::getStudentYearInfoId).toList();

        var classInfo = schoolYearClassRepository.findById(data.getClassId()).orElseThrow(()-> new NullPointerException("Không tìm thấy thông tin lớp học."));
        var students = studentYearInfoRepository.findAllByIdIn(stdIds);

        if(students.isEmpty()) throw new NullPointerException("Danh sách học sinh không hợp lệ.");
        if(students.stream().anyMatch(e->!e.getSchoolYearClass().equals(classInfo))) throw new ArgumentNotValidException("Danh sách học sinh không thuộc lớp học này.","","");

        // get day of week from date

        var dayOfWeek =  Calendar.getInstance();
        dayOfWeek.setTime(data.dayOff);
        var dow = dayOfWeek.get(Calendar.DAY_OF_WEEK);
        if(dow == Calendar.SUNDAY || dow == Calendar.SATURDAY) throw new ArgumentNotValidException("Không thể điểm danh vào ngày nghỉ.","","");

        var attendance = attendanceRepository.getAttendanceClassWithDayOff(data.getClassId(),data.getDayOff());

        if(!attendance.isEmpty()){
            data.getListStudent().forEach(e->{
                var std = attendance.stream().filter(a->a.getStudentYearInfo().getId().equals(e.getStudentYearInfoId())).findAny().orElseThrow();
                if(e.getId() == null && attendance.stream().anyMatch(a->a.getStudentYearInfo().getId().equals(e.getStudentYearInfoId())))
                    throw new ArgumentNotValidException("Học sinh "+e.getStudentYearInfoId()+" đã điểm danh.","","");
                if(e.getId() != null && !std.getId().equals(e.getId()))
                    throw new ArgumentNotValidException("Id điểm danh "+e.getId()+" không trùng khớp !!!","","");

            });
        }

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
        var teacherInfo = student.getSchoolYearClass().getTeacherSchoolYear().getTeacher().getUser();
        var studentinfo = student.getStudents();
        var title = studentinfo.getFirstName()+" "+studentinfo.getLastName()+" Xin nghỉ học";
        var body = data.getNote()+"\t"+ data.getStartDate()+"\t"+data.getEndDate();
        userNotificationRepository.save(UserNotification.builder()
                .title(title)
                .content(body)
                .user(teacherInfo)
                .createdAt(currentDate)
                .build());
        // send notify to parent
        if(!teacherInfo.getUserDeviceTokens().isEmpty()){
            fcmService.sendAllNotification(UserNotifyRes.builder()
                    .title(title)
                    .body(body)
                    .tokens(teacherInfo.getUserDeviceTokens().stream().map(UserDeviceToken::getDeviceToken).toList())
                    .build());
        }

        var res = takeLeaveRepository.save(takeLeave);

        return ResponseEntity.ok(res);
    }

    public ResponseEntity<?> getTakeLeave(Long parentId,Long studentId){
        return ResponseEntity.ok(takeLeaveRepository.findAllByParent_IdOrStudentYearInfo_Id(parentId,studentId).stream().map(TakeLeaveRes::toResParent).toList());
    }


    private Date localDateToDate(LocalDate date){
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}