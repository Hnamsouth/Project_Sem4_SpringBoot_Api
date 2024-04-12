package com.example.project_sem4_springboot_api.controller.student;

import com.example.project_sem4_springboot_api.dto.TeacherDetailsDto;
import com.example.project_sem4_springboot_api.dto.TeacherDto;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.entities.Teacher;
import com.example.project_sem4_springboot_api.service.TeacherService;
import com.example.project_sem4_springboot_api.service.impl.TeacherServiceImpl;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherServiceImpl teacherService;



    @PostMapping("/add-teacher")
    public ResponseEntity<TeacherDto> createTeacher(@RequestBody TeacherDetailsDto teacherDto, @RequestParam Long userId) {
//        TeacherDto createdTeacherDto = teacherService.createTeacher(teacherDto, userId).getDto();
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/teachers")
    public ResponseEntity<?> getTeacher(@Nullable Long id){
        return teacherService.getTeacher(id);
    }

    @PutMapping("/student/{studentId}")
    public Teacher updateTeacher(@PathVariable Long teacherId, @RequestBody TeacherDetailsDto teacher)throws Exception {
        Teacher findId = teacherService.findTeacherById(teacherId);
        Teacher updatedTeacher = teacherService.updateTeacher(teacher, findId.getId());
        return updatedTeacher;
    }

    @DeleteMapping("/teacher/{teacherId}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable Long teacherId){
        boolean deleted = teacherService.deleteTeacher(teacherId);
        if (deleted){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    /*
    * get contact teacher by id parent
    *
    * */
    @GetMapping("/teacher-contact")
    public ResponseEntity<?> getTeacherContact(  @Nullable Long studentId , @Nullable Long schoolYearClassId){
        return teacherService.getContactTeacher(studentId,schoolYearClassId);
    }


}
