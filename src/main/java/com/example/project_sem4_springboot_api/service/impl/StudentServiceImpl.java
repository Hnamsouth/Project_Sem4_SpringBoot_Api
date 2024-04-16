package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.StudentDto;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.repositories.StudentRepository;
import com.example.project_sem4_springboot_api.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {


    private final StudentRepository studentRepository;

    @Override
    public Student addStudent(StudentDto studentDto) {
        Student student = new Student();

        if (studentAlreadyExists(studentDto.getEmail())){
            throw new NullPointerException(studentDto.getEmail()+ " already exist");
        }

        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setAddress(studentDto.getAddress());
        student.setGender(studentDto.isGender());
        student.setEmail(studentDto.getEmail());
        student.setBirthday(studentDto.getBirthday());
        student.setStatus(studentDto.getStatus());
        student.setStudentCode(studentDto.getStudentCode());

        return studentRepository.save(student);

    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(()->new NullPointerException("Sorry, no student found with the ID: " + id));
    }

    @Override
    public Student updateStudent(Long id, StudentDto studentDto) {
        return studentRepository.findById(id).map(st -> {
            st.setFirstName(studentDto.getFirstName());
            st.setLastName(studentDto.getLastName());
            st.setEmail(studentDto.getEmail());
            st.setAddress(studentDto.getAddress());
            st.setGender(studentDto.isGender());
            st.setBirthday(studentDto.getBirthday());
            st.setStatus(studentDto.getStatus());
            st.setStudentCode(studentDto.getStudentCode());
            return studentRepository.save(st);
        }).orElseThrow(()->new NullPointerException("Sorry, this student cloud not be found"));
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)){
            throw new NullPointerException("Sorry, Student not found");
        }
        studentRepository.deleteById(id);
    }

    private boolean studentAlreadyExists(String email) {
        return studentRepository.findByEmail(email).isPresent();
    }

    @Override
    public List<Student> findStudentByClass(Long classId) {
        return null;
    }

    @Override
    public List<StudentDto> getAllStudentByName(String firstName) {
        return null;
    }
}