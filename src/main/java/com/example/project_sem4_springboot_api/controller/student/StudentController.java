package com.example.project_sem4_springboot_api.controller.student;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("")
    public ResponseEntity<StudentDto> createStudent(@ModelAttribute StudentDto studentDto){
        StudentDto studentDto1 = studentService.createStudent(studentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(studentDto1);
    }

    @GetMapping("/students")
    public ResponseEntity<List<StudentDto>> getAllStudent(){
        List<StudentDto> studentDtos = studentService.getAllStudent();
        return ResponseEntity.ok(studentDtos);
    }

    @GetMapping("/search/{firstName}")
    public ResponseEntity<List<StudentDto>> getAllStudentByFirstName(@PathVariable String firstName){
        List<StudentDto> studentDtos = studentService.getAllStudentByName(firstName);
        return ResponseEntity.ok(studentDtos);
    }

    @PutMapping("/student/{studentId}")
    public Student updateStudent(@PathVariable Long studentId, @RequestBody Student student)throws Exception {
        Student findId = studentService.findStudentById(studentId);
        Student updatedStudent = studentService.updateStudent(student, findId.getId());
        return updatedStudent;
    }

    @DeleteMapping("/student/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId){
        boolean deleted = studentService.deleteStudent(studentId);
        if (deleted){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Student>> findStudentByClass(@PathVariable Long classId) {
        List<Student> students = studentService.findStudentByClass(classId);
        return ResponseEntity.ok(students);
    }


}
