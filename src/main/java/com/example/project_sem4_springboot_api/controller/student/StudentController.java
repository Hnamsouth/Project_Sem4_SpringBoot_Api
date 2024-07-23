package com.example.project_sem4_springboot_api.controller.student;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.entities.request.AttendanceCreateOrUpdate;
import com.example.project_sem4_springboot_api.entities.request.TakeLeaveRequest;
import com.example.project_sem4_springboot_api.entities.response.ResultPaginationDto;
import com.example.project_sem4_springboot_api.service.impl.ExcelUploadService;
import com.example.project_sem4_springboot_api.service.impl.StudentServiceImpl;
import com.example.project_sem4_springboot_api.utils.ExcelUtils;
import com.turkraft.springfilter.boot.Filter;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/create-student-transactions")
    public ResponseEntity<?> createStudentTransactions(@RequestParam Long feePeriodId){
        return studentService.createStudentTransaction(feePeriodId);
    }
    @GetMapping("/students")
    public ResponseEntity<ResultPaginationDto> getAllStudent(@Filter Specification<Student> specification, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(studentService.getAllStudent(specification, pageable));
    }


    @GetMapping("/student-transfer-success")
    public ResponseEntity<?> studentTransferSuccess(@RequestParam Long studentTransactionId,@RequestParam String transactionCode){
        return studentService.transferSuccess(studentTransactionId,transactionCode);
    }

    @GetMapping("/get-all-student")
    public ResponseEntity<List<Student>> getAllStudent(){
        return ResponseEntity.ok().body(this.studentService.getAllStudent());
    }

    @GetMapping("/download")
    private ResponseEntity<InputStreamResource> download() throws IOException {
        String fileName ="student.xlsx";
        ByteArrayInputStream inputStream = studentService.getDataDownloaded();
        InputStreamResource response = new InputStreamResource(inputStream);

        ResponseEntity<InputStreamResource> responseEntity = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename="+fileName)
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(response);
        return responseEntity;
    }

    @PostMapping("/excel/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (ExcelUtils.hasExcelFormat(file)) {
            try {
                studentService.save(file);
                message = "The Excel file is uploaded: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(message);
            } catch (Exception exp) {
                message = "Failed to upload the Excel file: " + file.getOriginalFilename() + "! Error: " + exp.getMessage();
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
        }
        message = "Please upload an excel file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

//    @GetMapping("/student-list")
//    public ResponseEntity<?> getStudents() {
//        Map<String, Object> respStu = new LinkedHashMap<String, Object>();
//        List<Student> studList = studentService.findAll();
//        if (!studList.isEmpty()) {
//            respStu.put("status", 1);
//            respStu.put("data", studList);
//            return new ResponseEntity<>(respStu, HttpStatus.OK);
//        } else {
//            respStu.clear();
//            respStu.put("status", 0);
//            respStu.put("message", "Data is not found");
//            return new ResponseEntity<>(respStu, HttpStatus.NOT_FOUND);
//        }
//    }



}
