package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.StudentYearInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentYearInfoRepository extends JpaRepository<StudentYearInfo, Long> {
}
