package com.example.project_sem4_springboot_api.controller;

import com.example.project_sem4_springboot_api.dto.HomeWorkDetailDto;
import com.example.project_sem4_springboot_api.dto.StudentYearHomeWorkDto;
import com.example.project_sem4_springboot_api.entities.HomeWork;
import com.example.project_sem4_springboot_api.entities.StudentYearHomeWork;
import com.example.project_sem4_springboot_api.entities.request.HomeWorkDto;
import com.example.project_sem4_springboot_api.entities.request.HomeWorkRequest;
import com.example.project_sem4_springboot_api.entities.request.ImageDTO;
import com.example.project_sem4_springboot_api.service.impl.HomeWorkService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class HomeWorkController {

    private final HomeWorkService homeWorkService;

    public HomeWorkController(HomeWorkService homeWorkService) {
        this.homeWorkService = homeWorkService;
    }

    @PostMapping(consumes = {"multipart/form-data"},value="/createHomeWork")
    public ResponseEntity<HomeWork> createHomeWork(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String dueDate,
            @RequestParam Long teacherSchoolYearClassSubjectId,
            @RequestParam List<MultipartFile> images) throws ParseException, IOException {
        HomeWork createdHomeWork = homeWorkService.createHomeWork(title, content, dueDate, teacherSchoolYearClassSubjectId, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHomeWork);
    }

    @PostMapping(consumes = {"multipart/form-data"},value="/createStudentHomeWork")
    public ResponseEntity<StudentYearHomeWork> submitHomeWork(
            @RequestParam List<MultipartFile> images,
            @RequestParam String description,
            @RequestParam Long studentYearInfoId,
            @RequestParam Long homeWorkId) throws Exception {
        StudentYearHomeWork createdStudentYearHomeWork = homeWorkService.submitHomeWork(images, description, studentYearInfoId, homeWorkId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudentYearHomeWork);
    }

    @GetMapping("/getHomeWorkByStudentYearInfoID")
    public ResponseEntity<List<HomeWorkDto>> getHomeWorksByStudentYearInfoId(@RequestParam("id") Long studentYearInfoId) {
        List<HomeWorkDto> homeWorkDTOs = homeWorkService.getHomeWorksByStudentYearInfoId(studentYearInfoId);
        return ResponseEntity.ok(homeWorkDTOs);
    }

    /*
         4: lấy ds bài tập đã giao của giáo viên theo id phân công giảng dạy (techer_schoolyear_class_subject)
        DB(HomeWork)  , method: GET , params: techer_schoolyear_class_subject_id

         return : trả về danh sách bài tập + (số lượng hs đã đã nộp / sl hs của lớp )
         trả về ds url ảnh bài tập và ảnh bài nộp nếu có
    */
    @GetMapping("/getHomeWorksByTeacherSchoolYearClassSubject")
    public ResponseEntity<?> getHomeWorksByTeacherSchoolYearClassSubjectId(
            @RequestParam Long teacherSchoolYearClassSubjectId) {
        return ResponseEntity.ok(homeWorkService.getHomeWorksByTeacher(teacherSchoolYearClassSubjectId));
    }

    @GetMapping("/getHomeWorkDetail")
    public ResponseEntity<HomeWorkDto> getHomeWorkDetail(@RequestParam("homeWorkId") Long homeWorkId) {
        return ResponseEntity.ok(homeWorkService.getHomeWorkDetail(homeWorkId));
    }


}
