package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Excel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExcelRepository extends JpaRepository<Excel, Long> {
}
