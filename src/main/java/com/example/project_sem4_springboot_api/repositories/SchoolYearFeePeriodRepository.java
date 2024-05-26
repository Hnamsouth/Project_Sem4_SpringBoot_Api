package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.SchoolYearFeePeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolYearFeePeriodRepository extends JpaRepository<SchoolYearFeePeriod,Long> {
    List<SchoolYearFeePeriod> findAllByIdIn(List<Long> listIds);
}
