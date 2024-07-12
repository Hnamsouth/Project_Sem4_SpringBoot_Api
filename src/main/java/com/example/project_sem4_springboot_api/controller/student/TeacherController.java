package com.example.project_sem4_springboot_api.controller.student;

import com.example.project_sem4_springboot_api.dto.TeacherDetailsDto;
import com.example.project_sem4_springboot_api.dto.TeacherUpdateDto;
import com.example.project_sem4_springboot_api.entities.Teacher;
import com.example.project_sem4_springboot_api.entities.response.ResultPaginationDto;
import com.example.project_sem4_springboot_api.service.impl.TeacherServiceImpl;
import com.turkraft.springfilter.boot.Filter;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherServiceImpl teacherService;

    @GetMapping
    public ResponseEntity<?> getTeacher(@Nullable @RequestParam Long id,@Nullable @RequestParam boolean status){
        return teacherService.getTeacher(status,id);
    }
    @PostMapping()
    public ResponseEntity<?> createTeacher(@RequestBody TeacherDetailsDto teacherDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(teacherService.createTeacher(teacherDto));
    }

    @GetMapping("/teachers")
    public ResponseEntity<ResultPaginationDto> getAllTeacher(@Filter Specification<Teacher> specification, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(teacherService.getAllTeacher(specification, pageable));
    }


    @PutMapping()
    public ResponseEntity<?> updateTeacher( @RequestBody TeacherUpdateDto teacher){
        return teacherService.updateTeacher(teacher);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteTeacher(@RequestParam Long id){
        return teacherService.deleteTeacher(id);
    }

    /**
    * get contact teacher by id parent
    *
    * */
    @GetMapping("/contact")
    public ResponseEntity<?> getTeacherContact( @Nullable Long schoolYearClassId){
        return teacherService.getContactTeacher(schoolYearClassId);
    }
    @GetMapping("/getClasses")
    public ResponseEntity<?> getTeacherSchoolYearClass(@RequestParam Long schoolYearId){
        return teacherService.getSchoolYearClassByTeacher(schoolYearId);
    }
    @GetMapping("/getSubjects")
    public ResponseEntity<?> getTeacherSchoolYearSubject(@RequestParam Long schoolYearId){
        return teacherService.ge(schoolYearId);
    }


}
