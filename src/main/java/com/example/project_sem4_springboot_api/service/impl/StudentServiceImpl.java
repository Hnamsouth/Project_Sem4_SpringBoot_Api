package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.entities.StudentStatus;
import com.example.project_sem4_springboot_api.entities.StudentYearInfo;
import com.example.project_sem4_springboot_api.entities.enums.ESem;
import com.example.project_sem4_springboot_api.entities.enums.EStudentStatus;
import com.example.project_sem4_springboot_api.exception.ArgumentNotValidException;
import com.example.project_sem4_springboot_api.repositories.*;
import com.example.project_sem4_springboot_api.service.StudentService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl  {

    private final String STUDENT_STATUS_CREATE=EStudentStatus.STUDENT_DANG_HOC.name();
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
        StudentYearInfo studentYearInfo = StudentYearInfo.builder().students(newStudent).schoolYearClass(schoolYearClass).createdAt(newDate).build();
        studentYearInfoRepository.save(studentYearInfo);

        return ResponseEntity.ok(newStudent);
    }

    public ResponseEntity<?> getStudentInfoBy(
            Long bySchoolYearClassId,
            Long bySchoolYearId,
            Long statusId,
            String name,
            String studentCode,
            Integer pagination
    ) {
        if((bySchoolYearClassId!=null && bySchoolYearId!=null) || (bySchoolYearClassId==null && bySchoolYearId==null))  throw new ArgumentNotValidException("Yêu cầu 1 trong 2 tham số bySchoolYearClassId hoặc bySchoolYearId","","");
        PageRequest pageRequest = PageRequest.of(0, pagination, Sort.by("createdAt").descending());
        var studentInfo = studentYearInfoRepository.findAllBySchoolYearClass_IdOrSchoolYearClass_SchoolYear_IdOrStudents_StudentCode(bySchoolYearClassId, bySchoolYearId,studentCode,pageRequest).toList();
        if(studentInfo.isEmpty()) throw new NullPointerException("Không tìm thấy thông tin học sinh.");
        var rs = studentInfo.stream().map(e->{   e.setStudents(e.getStudents().toResInfo()); return e;   }).toList();
        if(statusId!=null){
            rs = rs.stream().filter(e->e.getStudents().getStudentStatuses().get(0).getStatus().getId().equals(statusId)).toList();
        }
        if(name!=null){
            rs = rs.stream().filter(e->{
                var fullname = e.getStudents().getFirstName()+" "+e.getStudents().getLastName().toLowerCase();
                return fullname.contains(name.toLowerCase());
            }).toList();
        }
        return ResponseEntity.ok(rs);
    }

    public ResponseEntity<?> createAttendance(Map<String, Object> data) {
        return null;
    }

    public ResponseEntity<?> deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) throw new NullPointerException("Không tìm thấy học sinh!!!");
        studentRepository.deleteById(id);
        return ResponseEntity.ok("Đã xóa học sinh.");
    }


}