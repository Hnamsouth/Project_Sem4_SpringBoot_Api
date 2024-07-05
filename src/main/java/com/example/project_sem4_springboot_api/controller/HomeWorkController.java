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

    @PostMapping("/createHomework")
    public ResponseEntity<HomeWork> createHomeWork(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String dueDate,
            @RequestParam Long teacherSchoolYearClassSubjectId,
            @RequestParam List<MultipartFile> images) throws ParseException {
        HomeWork createdHomeWork = homeWorkService.createHomeWork(title, content, dueDate, teacherSchoolYearClassSubjectId, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHomeWork);
    }

    @PostMapping("/createStudentYearHomeWork")
    public ResponseEntity<StudentYearHomeWork> submitHomeWork(
            @RequestParam List<MultipartFile> images,
            @RequestParam String description,
            @RequestParam Long studentYearInfoId,
            @RequestParam Long homeWorkId) {
        StudentYearHomeWork createdStudentYearHomeWork = homeWorkService.submitHomeWork(images, description, studentYearInfoId, homeWorkId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudentYearHomeWork);
    }

    @GetMapping("/getHomeWorkByStudentYearInfoID")
    public ResponseEntity<List<HomeWorkDto>> getHomeWorksByStudentYearInfoId(@RequestParam("id") Long studentYearInfoId) {
        System.out.println("id" + studentYearInfoId);
        List<HomeWorkDto> homeWorkDTOs = homeWorkService.getHomeWorksByStudentYearInfoId(studentYearInfoId);

        return ResponseEntity.ok(homeWorkDTOs);
    }



//    private final HomeWorkService homeWorkService;
//
//    public HomeWorkController(HomeWorkService homeWorkService) {
//        this.homeWorkService = homeWorkService;
//    }

//    @PostMapping("/createHomework")
//    public ResponseEntity<HomeWork> createHomeWork(@RequestBody HomeWorkDto homeWorkDto) {
//        HomeWork createdHomeWork = homeWorkService.createHomeWork(homeWorkDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdHomeWork);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<HomeWorkDetailDto> getHomeWorkDetails(@PathVariable Long id) {
//        HomeWorkDetailDto homeWorkDetailDto = homeWorkService.getHomeWorkDetails(id);
//        return ResponseEntity.ok(homeWorkDetailDto);
//    }
//
//    @GetMapping("/student-year/{studentYearInfoId}")
//    public ResponseEntity<List<HomeWorkDto>> getHomeWorksByStudentYearInfoId(@PathVariable Long studentYearInfoId) {
//        List<HomeWorkDto> homeworkDtos = homeWorkService.getHomeWorksByStudentYearInfoId(studentYearInfoId);
//        return ResponseEntity.ok(homeworkDtos);
//    }
//
//    @GetMapping("/teacher-school-year-class-subject/{teacherSchoolYearClassSubjectId}")
//    public ResponseEntity<List<HomeWorkDto>> getHomeWorksByTeacherSchoolYearClassSubjectId(@PathVariable Long teacherSchoolYearClassSubjectId) {
//        List<HomeWorkDto> homeworkDtos = homeWorkService.getHomeWorksByTeacherSchoolYearClassSubjectId(teacherSchoolYearClassSubjectId);
//        return ResponseEntity.ok(homeworkDtos);
//    }



}
