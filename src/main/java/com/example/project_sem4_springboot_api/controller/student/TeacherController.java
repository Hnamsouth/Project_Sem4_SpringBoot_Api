package com.example.project_sem4_springboot_api.controller.student;

import com.example.project_sem4_springboot_api.dto.TeacherDetailsDto;
import com.example.project_sem4_springboot_api.dto.TeacherDto;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.entities.Teacher;
import com.example.project_sem4_springboot_api.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/add-teacher")
    public ResponseEntity<TeacherDto> createTeacher(@RequestBody TeacherDetailsDto teacherDto, @RequestParam Long userId) {
        TeacherDto createdTeacherDto = teacherService.createTeacher(teacherDto, userId).getDto();
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeacherDto);
    }

    @GetMapping("/teachers")
    public ResponseEntity<List<TeacherDto>> getAllTeacher(){
        List<TeacherDto> teacherDtoList = teacherService.getAllTeacher();
        return ResponseEntity.ok(teacherDtoList);
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


}
