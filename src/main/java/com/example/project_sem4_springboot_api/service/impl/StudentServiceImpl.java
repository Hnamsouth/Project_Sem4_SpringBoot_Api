package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.exception.StudentNotFoundException;
import com.example.project_sem4_springboot_api.repositories.StudentRepository;
import com.example.project_sem4_springboot_api.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    public StudentDto createStudent(StudentDto studentDto) {
        Student student = new Student();
        student.setGender(studentDto.isGender());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setBirthday(studentDto.getBirthday());
        student.setAddress(studentDto.getAddress());
        student.setStatus(studentDto.getStatus());
        student.setStudentCode(studentDto.getStudentCode());
        return studentRepository.save(student).getDto();
    }

    public List<StudentDto> getAllStudent(){
        List<Student> students = studentRepository.findAll();
        return students.stream().map(Student::getDto).collect(Collectors.toList());
    }

    public List<StudentDto> getAllStudentByName(String firstName){
        List<Student> students = studentRepository.findAllByFirstNameContaining(firstName);
        return students.stream().map(Student::getDto).collect(Collectors.toList());
    }

    @Override
    public Student updateStudent(Student student, Long studentId) throws Exception {
        Optional<Student> student1 = studentRepository.findById(studentId);
        if (student1.isEmpty()){
            throw new Exception("user not exits with id " + studentId);
        }
        Student oldStudent = student1.get();

        if (student.getFirstName()!=null){
            oldStudent.setFirstName(student.getFirstName());
        }
        if (student.getLastName()!=null){
            oldStudent.setLastName(student.getLastName());
        }
        if (student.isGender()){
            oldStudent.setGender(student.isGender());
        }
        if (student.getBirthday()!= null){
            oldStudent.setBirthday(student.getBirthday());
        }
        if (student.getAddress()!= null){
            oldStudent.setAddress(student.getAddress());
        }
        if (student.getStatus()!= 1){
            oldStudent.setStatus(student.getStatus());
        }
        if (student.getStudentCode()!= null){
            oldStudent.setStudentCode(student.getStudentCode());
        }
        Student updatedStudent = studentRepository.save(oldStudent);
        return updatedStudent;
    }

    public boolean deleteStudent(Long id){
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()){
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Student findStudentById(Long studentId) throws Exception {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()){
            return student.get();
        }
        throw new Exception("user not exits with user id " + studentId);
    }

}
