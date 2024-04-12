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
     * 6: create teacher schoolyear subject
     * 7: create schedule
     * */
    private final SchoolServiceImpl schoolService;

    @PostMapping("/creat-school-year")
    public ResponseEntity<?> createSchoolYear (@Valid @RequestBody SchoolYearCreate data){
        return schoolService.createSchoolYear(data);
    }
    @PostMapping("/creat-school-year_subject")
    public ResponseEntity<?> createSchoolYearSubject (@Valid @RequestBody SchoolYearSubjectCreate data){
        return schoolService.createSchoolYearSubject(data);
    }
    @PostMapping("/creat-teacher-school-year")
    public ResponseEntity<?> createTeacherSchoolYear (@Valid @RequestBody TeacherSchoolYearCreate data){
        return schoolService.createTeacherSchoolYear(data);
    }
    @PostMapping(value = "/creat-school-year-class")
    @Description("create school year class")
    public ResponseEntity<?> createSchoolYearClass (@Valid @RequestBody SchoolYearClassCreate data){
        return schoolService.createSchoolYearClass(data);
    }

    /**
     * creat-school-year-subject-grade: phân phối chương trình học
     *
     * @table schoolyear_class_subject
     * @requestBody SchoolYearSubjectGradeCreate
     * @return List<SchoolYearSubjectGrade>
     * */
    @PostMapping(value = "/creat-school-year-subject-grade")
    public ResponseEntity<?> createSchoolYearSubjectGrade (@Valid @RequestBody SchoolYearSubjectGradeCreate data){
        return schoolService.createSchoolYearSubjectGrade(data);
    }

    /**
     * creat-teacher-school-year-class-subject: phân công giảng dạy
     *
     * @table teacher_schoolyear_class_subject
     * @requestBody TeacherSchoolYearClassSubjectCreate
     * @return List<TeacherSchoolYearClassSubject>
     * */
    @PostMapping("/creat-teacher-school-year-class-subject")
    public ResponseEntity<?> createTeacherSchoolYearClassSubject (@Valid @RequestBody TeacherSchoolYearClassSubjectCreate data){
        return schoolService.createTeacherSchoolYearClassSubject(data);
    }

    @PostMapping("/creat-schedule")
    public ResponseEntity<?> createSchedule (@Valid @RequestBody ScheduleCreate data){
        return schoolService.createSchedule(data);
    }
    /*
     * READ & SEARCH
     * 1: read school year
     * 2: read schoolyear_subject
     * 3: read teacher_schoolyear
     * 4: read schoolyear_class
     * 5: read schoolyear-subject-grade
     * */

    /**
     * Get subject
     *
     * @param id nullable
     * @return {Subject or List<Subject>}
     * */
    @GetMapping("/subject")
    public ResponseEntity<?> get_Subject( @RequestParam @Nullable Long id){
        return schoolService.getSubject(id);
    }
    @GetMapping("/teacher")
    public ResponseEntity<?> get_Teacher( @RequestParam @Nullable Long id){
        return schoolService.getTeacher(id);
    }

    /**
     * Get school year
     *
     * @param id nullable
     * @return {SchoolYear or List<SchoolYear>}
     * */
    @GetMapping("/school-year")
    public ResponseEntity<?> get_SchoolYear( @Nullable @RequestParam Long id){
        return schoolService.getSchoolYear(id);
    }

    /**
     * Get school year subject
     *
     * @param id nullable
     * @param schoolYearId nullable
     * @param subjectIds nullable
     * @return {SchoolYearSubject or List<SchoolYearSubject>}
     * */
    @GetMapping("/school-year-subject")
    public ResponseEntity<?> get_SchoolYear(
            @Nullable @RequestParam Long id,
            @Nullable @RequestParam Long schoolYearId,
            @Nullable @RequestParam List<Long> subjectIds
    ){
        return schoolService.getSchoolYearSubject(id,schoolYearId,subjectIds);
    }

    /**
     * Get teacher school year
     *
     * @description get teacher school year
     * @return {TeacherSchoolYear or List<TeacherSchoolYear>}
     * */
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

    /**
     * get teacher school year
     *
     * @description Get all or get by -id or filter by -teacherId and -schoolYearId
     * */
    @GetMapping("/teacher-school-year")
    public ResponseEntity<?> get_TeacherSchoolYear(
            @Nullable @RequestParam Long id,
            @Nullable @RequestParam Long teacherId,
            @Nullable @RequestParam Long schoolYearId
    ){
        return schoolService.getTeacherSchoolYear(id,teacherId,schoolYearId);
    }

    /**
     * Get school-year-subject-grade : phân phối chương trình học
     *
     * @description
     * */
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
    public ResponseEntity<?> get_Schedule(
            @Nullable @RequestParam Long classId,
            @Nullable @RequestParam Long teacherSchoolYearId,
            @Nullable @RequestParam Long gradeId,
            @Nullable @RequestParam Long schoolYearId){
        return schoolService.getSchedule(classId,teacherSchoolYearId,gradeId,schoolYearId);
    }

}
