package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.entities.StudentStatus;
import com.example.project_sem4_springboot_api.entities.StudentYearInfo;
import com.example.project_sem4_springboot_api.entities.enums.EStatus;
import com.example.project_sem4_springboot_api.entities.request.AttendanceCreate;
import com.example.project_sem4_springboot_api.exception.ArgumentNotValidException;
import com.example.project_sem4_springboot_api.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl  {

    private final String STUDENT_STATUS_CREATE= EStatus.STUDENT_DANG_HOC.name();
    private final StudentRepository studentRepository;
    private final StudentStatusRepository studentStatusRepository;
    private final StatusRepository statusRepository;
    private final StudentYearInfoRepository studentYearInfoRepository;
    private final SchoolYearClassRepository schoolYearClassRepository;

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
            Long statusId,
            String byNameOrCode,
            Integer limit
    ) {
        if((bySchoolYearClassId!=null && bySchoolYearId!=null) || (bySchoolYearClassId==null && bySchoolYearId==null))  throw new ArgumentNotValidException("Yêu cầu 1 trong 2 tham số bySchoolYearClassId hoặc bySchoolYearId","","");
        var studentInfo = limit != null ?  studentYearInfoRepository.findAllBySchoolYearClass_IdOrSchoolYearClass_SchoolYear_Id(bySchoolYearClassId, bySchoolYearId,
                PageRequest.of(0, limit, Sort.by("createdAt").descending())).toList()
                : studentYearInfoRepository.findAllBySchoolYearClass_IdOrSchoolYearClass_SchoolYear_Id(bySchoolYearClassId, bySchoolYearId);
        if(studentInfo.isEmpty()) throw new NullPointerException("Không tìm thấy thông tin học sinh.");
        var rs = studentInfo.stream().map(e->{ e.setStudents(e.getStudents().toResInfo()); return e;   }).toList();
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

    public ResponseEntity<?> createAttendance( List<AttendanceCreate> data) {
        return ResponseEntity.ok(data);
    }


}