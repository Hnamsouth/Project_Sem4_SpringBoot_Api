package com.example.project_sem4_springboot_api.controller.student;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.example.project_sem4_springboot_api.entities.Attendance;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.entities.response.ResultPaginationDto;
import com.example.project_sem4_springboot_api.service.impl.StudentServiceImpl;
import com.turkraft.springfilter.boot.Filter;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentServiceImpl studentService;

    @PostMapping
    public ResponseEntity<?> create_Student(@Valid @RequestBody StudentDto data){
       return studentService.createStudent(data);
    }

    @GetMapping("/get-student-year-info-by")
    public ResponseEntity<?> get_StudentInfoBy(
            @RequestParam @Nullable Long bySchoolYearClassId,
            @RequestParam @Nullable Long bySchoolYearId,
            @RequestParam @Nullable Long byParentId,
            @RequestParam @Nullable Long byStatusId,
            @RequestParam @Nullable String byNameOrCode,
            @RequestParam  @Nullable Integer limit
    ){
        return studentService.getStudentInfoBy(bySchoolYearClassId,bySchoolYearId,byParentId,byStatusId,byNameOrCode,limit);
    }


    @PutMapping("/update-student-info")
    public ResponseEntity<?> updateStudentInfo(@Valid @RequestBody Student data){
        return studentService.updateStudentInfo(data);
    }

    @PostMapping("/mark")
    public ResponseEntity<Attendance> markAttendance(
            @RequestParam Long studentYearInfoId,
            @RequestParam boolean status,
            @RequestParam(required = false) String note) {
        Attendance attendance = studentService.markAttendance(studentYearInfoId, status, note);
        return ResponseEntity.ok(attendance);
    }

    @GetMapping("/students")
    public ResponseEntity<ResultPaginationDto> getAllStudent(@Filter Specification<Student> specification, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getAllStudent(specification, pageable));
    }


//    @PostMapping("/create-attendance")
//    public ResponseEntity<?> createAttendance(
//        @Validated @RequestBody
//        @NotEmpty(message = "Danh sách điểm danh không được để trống!!!")
//        List<AttendanceCreate> data
//    ){
//        return studentService.createAttendance(data);
//    }

}
