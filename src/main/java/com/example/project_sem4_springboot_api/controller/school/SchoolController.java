package com.example.project_sem4_springboot_api.controller.school;

import com.example.project_sem4_springboot_api.entities.enums.ESem;
import com.example.project_sem4_springboot_api.entities.request.*;
import com.example.project_sem4_springboot_api.service.impl.SchoolServiceImpl;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/school")
@RequiredArgsConstructor
public class SchoolController {
    /*
     * 1: create school year
     * 2: create schoolyear_subject
     * 3: create teacher_schoolyear
     * 4: create schoolyear_class
     * 5: create schoolyear-subject-grade
     * */
    private final SchoolServiceImpl schoolService;

    @PostMapping("/creat-school-year")
    public ResponseEntity<?> createSchoolYear (@RequestBody SchoolYearDto data){
        return schoolService.createSchoolYear(data);
    }
    @PostMapping("/creat-school-year_subject")
    public ResponseEntity<?> createSchoolYearSubject (@Valid @RequestBody SchoolYearSubjectDto data){
        return schoolService.createSchoolYearSubject(data);
    }
    @PostMapping("/creat-teacher-school-year")
    public ResponseEntity<?> createTeacherSchoolYear (@RequestBody TeacherSchoolYearDto data){
        return schoolService.createTeacherSchoolYear(data);
    }
    @PostMapping(value = "/creat-school-year-class")
    @Description("create school year class")
    public ResponseEntity<?> createSchoolYearClass (@RequestBody SchoolYearClassDto data){
        return schoolService.createSchoolYearClass(data);
    }
    @PostMapping(value = "/creat-school-year-subject-grade")
    @Description("create school year class")
    public ResponseEntity<?> createSchoolYearSubjectGrade (@Valid @RequestBody SchoolYearSubjectGradeCreate data){
        return schoolService.createSchoolYearSubjectGrade(data);
    }
    /*
    * read:
    *   school year
    *
    * */

    @GetMapping("/subject")
    public ResponseEntity<?> get_Subject( @RequestParam @Nullable Long id){
        return schoolService.getSubject(id);
    }
    @GetMapping("/school-year")
    public ResponseEntity<?> get_SchoolYear( @Nullable @RequestParam Long id){
        return schoolService.getSchoolYear(id);
    }
    @GetMapping("/school-year-subject")
    public ResponseEntity<?> get_SchoolYear(
            @Nullable @RequestParam Long id,
            @Nullable @RequestParam Long schoolYearId,
            @Nullable @RequestParam List<Long> subjectIds
    ){
        return schoolService.getSchoolYearSubject(id,schoolYearId,subjectIds);
    }

    @GetMapping("/school-year-class")
    public ResponseEntity<?> get_SchoolYearClass(
            @Nullable @RequestParam Long id,
            @Nullable @RequestParam Long gradeId,
            @Nullable @RequestParam Long schoolYearId,
            @Nullable @RequestParam Long teacherSchoolYearId,
            @Nullable @RequestParam Long roomId,
            @Nullable @RequestParam String className,
            @Nullable @RequestParam String classCode
    ){
        return schoolService.getSchoolYearClass(
                id,gradeId,schoolYearId,teacherSchoolYearId,roomId,className,classCode
        );
    }
    @GetMapping("/teacher-school-year")
    public ResponseEntity<?> get_TeacherSchoolYear(
            @Nullable @RequestParam Long id,
            @Nullable @RequestParam Long teacherId,
            @Nullable @RequestParam Long schoolYearId
    ){
        return schoolService.getTeacherSchoolYear(id,teacherId,schoolYearId);
    }
    @GetMapping("/school-year-subject-grade")
    public ResponseEntity<?> get_SchoolYearSubjectGrade(
            @Nullable @RequestParam Long id,
            @Nullable @RequestParam Long schoolYearSubjectId,
            @Nullable @RequestParam Long gradeId,
            @Nullable @RequestParam Integer number,
            @Nullable @RequestParam ESem sem
    ){
        return schoolService.getSchoolYearSubjectGrade(id,schoolYearSubjectId,gradeId,number,sem);
    }

    @GetMapping("/schedule")
    public ResponseEntity<?> get_Schedule( @RequestParam @Nullable Long id){
        return schoolService.getSchedule(id);
    }

}
