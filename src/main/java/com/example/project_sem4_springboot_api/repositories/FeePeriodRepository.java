package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.FeePeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeePeriodRepository extends JpaRepository<FeePeriod,Long> {
    List<FeePeriod> findAllBySchoolyear_Id(Long schoolYearId);
}
