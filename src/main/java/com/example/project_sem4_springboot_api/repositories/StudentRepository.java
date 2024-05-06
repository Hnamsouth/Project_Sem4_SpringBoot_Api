package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findAllByIdIn(List<Long> ids);

}
