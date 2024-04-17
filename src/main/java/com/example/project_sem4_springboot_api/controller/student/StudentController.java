package com.example.project_sem4_springboot_api.controller.student;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.service.impl.StudentServiceImpl;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentServiceImpl studentService;

    @PostMapping
    public ResponseEntity<?> create_Student(@Valid @RequestBody StudentDto data){
       return studentService.createStudent(data);
    }

    @GetMapping("/get-student-year-info-by")
    public ResponseEntity<?> get_StudentInfoBy(
            @RequestParam @Nullable Long bySchoolYearClassId,
            @RequestParam @Nullable Long bySchoolYearId,
            @RequestParam @Nullable Long byStatusId,
            @RequestParam @Nullable String byNameOrCode,
            @RequestParam Integer pagination
    ){
        return studentService.getStudentInfoBy(bySchoolYearClassId,bySchoolYearId,byStatusId,byNameOrCode,pagination);
    }


    @PutMapping("/update-student-info")
    public ResponseEntity<?> updateStudentInfo(@Valid @RequestBody Student data){
        return studentService.updateStudentInfo(data);
    }
//
//    @GetMapping("/{id}")
//    public Student getStudentById(@PathVariable Long id){
//        return studentService.getStudentById(id);
//    }
//
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteStudent(@RequestParam @NotNull Long id){
        return studentService.deleteStudent(id);
    }
//
//
//    @GetMapping("/search/{firstName}")
//    public ResponseEntity<List<StudentDto>> getAllStudentByFirstName(@PathVariable String firstName){
//        List<StudentDto> studentDtos = studentService.getAllStudentByName(firstName);
//        return ResponseEntity.ok(studentDtos);
//    }
//
//    @GetMapping("/class/{classId}")
//    public ResponseEntity<List<Student>> findStudentByClass(@PathVariable Long classId) {
//        List<Student> students = studentService.findStudentByClass(classId);
//        return ResponseEntity.ok(students);
//    }


}
