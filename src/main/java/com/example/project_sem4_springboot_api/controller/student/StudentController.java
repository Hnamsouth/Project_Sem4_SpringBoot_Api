package com.example.project_sem4_springboot_api.controller.student;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.entities.request.AttendanceCreateOrUpdate;
import com.example.project_sem4_springboot_api.entities.request.TakeLeaveRequest;
import com.example.project_sem4_springboot_api.entities.response.ResultPaginationDto;
import com.example.project_sem4_springboot_api.service.impl.StudentServiceImpl;
import com.turkraft.springfilter.boot.Filter;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
    public ResponseEntity<?> markAttendance(@RequestBody AttendanceCreateOrUpdate data) {
        return ResponseEntity.ok(studentService.markAttendance(data));
    }

    @GetMapping("/get-attendance")
    public ResponseEntity<?> getAttendance(@RequestParam(required = false) Long studentYearInfoId, @RequestParam(required = false) Date date, @RequestParam (required = false) Long schoolYearClassId){
        return studentService.getAttendanceBy(studentYearInfoId,date,schoolYearClassId);
    }
    @PostMapping("/take-leave")
    public ResponseEntity<?> takeLeave(@Valid @RequestBody TakeLeaveRequest data) {
        return ResponseEntity.ok(studentService.takeLeave(data));
    }
    @GetMapping("/get-take-leave")
    public ResponseEntity<?> getTakeLeave(@RequestParam(required = false) Long userId,@RequestParam(required = false) Long studentId){
        return studentService.getTakeLeave(userId,studentId);
    }

    @GetMapping("/get-student-transactions")
    public ResponseEntity<?> getStudentTransactions(@RequestParam (required = false) Long feePeriodId, @RequestParam Long studentYearInfoId){
        return studentService.getStudentTransaction(feePeriodId,studentYearInfoId);
    }

    @GetMapping("/students")
    public ResponseEntity<ResultPaginationDto> getAllStudent(@Filter Specification<Student> specification, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getAllStudent(specification, pageable));
    }


    @GetMapping("/student-transfer-success")
    public ResponseEntity<?> studentTransferSuccess(@RequestParam Long studentTransactionId,@RequestParam String transactionCode){
        return studentService.transferSuccess(studentTransactionId,transactionCode);
    }

}
