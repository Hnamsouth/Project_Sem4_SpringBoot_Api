package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findAllByFirstNameContaining(String firstName);

    List<Student> findByStudentYearInfosSchoolYearClassId(Long classId);

    Optional<Student> findByEmail(String email);
//    List<Student> findByStudentYearInfosSchoolYearClassId(Long schoolYearClassId);
}
