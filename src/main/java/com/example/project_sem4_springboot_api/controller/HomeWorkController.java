package com.example.project_sem4_springboot_api.controller;

import com.example.project_sem4_springboot_api.dto.HomeWorkDetailDto;
import com.example.project_sem4_springboot_api.dto.StudentYearHomeWorkDto;
import com.example.project_sem4_springboot_api.entities.HomeWork;
import com.example.project_sem4_springboot_api.entities.StudentYearHomeWork;
import com.example.project_sem4_springboot_api.entities.request.HomeWorkDto;
import com.example.project_sem4_springboot_api.service.impl.HomeWorkService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HomeWorkController {

    private final HomeWorkService homeWorkService;

    public HomeWorkController(HomeWorkService homeWorkService) {
        this.homeWorkService = homeWorkService;
    }

    @PostMapping
    public ResponseEntity<HomeWork> createHomeWork(@RequestBody HomeWorkDto homeWorkDto) {
        HomeWork createdHomeWork = homeWorkService.createHomeWork(homeWorkDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdHomeWork);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HomeWorkDetailDto> getHomeWorkDetails(@PathVariable Long id) {
        HomeWorkDetailDto homeWorkDetailDto = homeWorkService.getHomeWorkDetails(id);
        return ResponseEntity.ok(homeWorkDetailDto);
    }

    @GetMapping("/student-year/{studentYearInfoId}")
    public ResponseEntity<List<HomeWorkDto>> getHomeWorksByStudentYearInfoId(@PathVariable Long studentYearInfoId) {
        List<HomeWorkDto> homeworkDtos = homeWorkService.getHomeWorksByStudentYearInfoId(studentYearInfoId);
        return ResponseEntity.ok(homeworkDtos);
    }

    @GetMapping("/teacher-school-year-class-subject/{teacherSchoolYearClassSubjectId}")
    public ResponseEntity<List<HomeWorkDto>> getHomeWorksByTeacherSchoolYearClassSubjectId(@PathVariable Long teacherSchoolYearClassSubjectId) {
        List<HomeWorkDto> homeworkDtos = homeWorkService.getHomeWorksByTeacherSchoolYearClassSubjectId(teacherSchoolYearClassSubjectId);
        return ResponseEntity.ok(homeworkDtos);
    }

//    @PostMapping
//    public ResponseEntity<StudentYearHomeWorkDto> createStudentYearHomeWork(@RequestBody StudentYearHomeWorkDto studentYearHomeWorkDto) {
//        StudentYearHomeWorkDto createdDto = homeWorkService.createStudentYearHomeWork(studentYearHomeWorkDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<HomeWorkDetailDto> getHomeWorkDetails(@PathVariable Long id) {
//        HomeWorkDetailDto homeWorkDetailDto = homeWorkService.getHomeWorkDetails(id);
//        return ResponseEntity.ok(homeWorkDetailDto);
//    }

}
