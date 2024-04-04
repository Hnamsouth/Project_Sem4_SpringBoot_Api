package com.example.project_sem4_springboot_api.controller.school;

import com.example.project_sem4_springboot_api.dto.SchoolYearClassDto;
import com.example.project_sem4_springboot_api.dto.SchoolYearDto;
import com.example.project_sem4_springboot_api.dto.SchoolYearSubjectDto;
import com.example.project_sem4_springboot_api.dto.TeacherSchoolYearDto;
import com.example.project_sem4_springboot_api.service.impl.SchoolServiceImpl;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/school")
@RequiredArgsConstructor
public class SchoolController {
    /*
     * 1: create school year
     * 2: create schoolyear_subject
     * 3: create teacher_schoolyear
     * 4: create schoolyear_class
     * */
    private final SchoolServiceImpl schoolService;

    @PostMapping("/creat-school-year")
    public ResponseEntity<?> createSchoolYear (@RequestBody SchoolYearDto data){
        return schoolService.createSchoolYear(data);
    }
    @PostMapping("/creat-school-year_subject")
    public ResponseEntity<?> createSchoolYearSubject (@RequestBody SchoolYearSubjectDto data){
        return schoolService.createSchoolYearSubject(data);
    }
    @PostMapping("/creat-teacher-school-year")
    public ResponseEntity<?> createTeacherSchoolYear (@RequestBody TeacherSchoolYearDto data){
        return schoolService.createTeacherSchoolYear(data);
    }
    @PostMapping("/creat-school-year-class")
    public ResponseEntity<?> createSchoolYearClass (@RequestBody SchoolYearClassDto data){
        return schoolService.createSchoolYearClass(data);
    }

    /*
    * read:
    *
    *
    * */

    @GetMapping("/subject")
    public ResponseEntity<?> get_Subject(@Nullable Long id){
        return schoolService.getSubject(id);
    }
    @GetMapping("/school-year")
    public ResponseEntity<?> get_SchoolYear(
        @Nullable @RequestParam Long id,
        @Nullable @RequestParam Date startSem1,
        @Nullable @RequestParam Date startSem2,
        @Nullable @RequestParam Date end
    ){
        return schoolService.getSchoolYear(id,startSem1,startSem2,end);
    }

}
