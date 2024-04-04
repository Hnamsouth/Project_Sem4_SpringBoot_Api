package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.SchoolYearSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolYearSubjectRepository extends JpaRepository<SchoolYearSubject,Long> {
    SchoolYearSubject findBySubject_Name(String name);
}
