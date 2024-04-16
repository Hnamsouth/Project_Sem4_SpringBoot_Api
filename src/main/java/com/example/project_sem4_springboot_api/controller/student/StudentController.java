package com.example.project_sem4_springboot_api.controller.student;

import com.example.project_sem4_springboot_api.dto.ApiResponse;
import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.service.StudentService;
import jakarta.validation.Valid;
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

    @PostMapping
    public ApiResponse<Student> addStudent(@RequestBody @Valid StudentDto studentDto){
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(studentService.addStudent(studentDto));
        return apiResponse;
    }

    @GetMapping
    public List<Student> getAllStudent(){
        return studentService.getStudents();
    }

    @PutMapping("/update/{id}")
    public ApiResponse<Student> updateStudent(@RequestBody StudentDto studentDto, @PathVariable Long id){
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setResult(studentService.updateStudent(id, studentDto));
        return apiResponse;
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id){
        return studentService.getStudentById(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
        return "Student has been deleted";
    }


    @GetMapping("/search/{firstName}")
    public ResponseEntity<List<StudentDto>> getAllStudentByFirstName(@PathVariable String firstName){
        List<StudentDto> studentDtos = studentService.getAllStudentByName(firstName);
        return ResponseEntity.ok(studentDtos);
    }

    @GetMapping("/class/{classId}")
    public ResponseEntity<List<Student>> findStudentByClass(@PathVariable Long classId) {
        List<Student> students = studentService.findStudentByClass(classId);
        return ResponseEntity.ok(students);
    }


}
