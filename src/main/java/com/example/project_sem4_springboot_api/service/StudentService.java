package com.example.project_sem4_springboot_api.service;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.example.project_sem4_springboot_api.entities.Student;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

public interface StudentService {

    Student addStudent(StudentDto studentDto);

    List<Student> getStudents();

    Student getStudentById(Long id);

    Student updateStudent(Long id, StudentDto studentDto);

    void deleteStudent(Long id);

    List<Student> findStudentByClass(Long classId);

    List<StudentDto> getAllStudentByName(String firstName);

    ByteArrayInputStream getDataDownloaded();

}
