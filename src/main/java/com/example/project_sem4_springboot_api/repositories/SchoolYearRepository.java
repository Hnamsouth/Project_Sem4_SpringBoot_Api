package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.SchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolYearRepository extends JpaRepository<SchoolYear, Long> {
}
