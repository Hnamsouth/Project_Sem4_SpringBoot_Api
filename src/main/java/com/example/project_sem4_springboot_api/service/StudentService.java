package com.example.project_sem4_springboot_api.service;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.example.project_sem4_springboot_api.entities.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    StudentDto createStudent(StudentDto studentDto);

    List<StudentDto> getAllStudent();

    List<StudentDto> getAllStudentByName(String firstName);

    Student updateStudent(Student student, Long studentId)throws Exception;

    boolean deleteStudent(Long id);

    Student findStudentById(Long studentId)throws Exception;

}
