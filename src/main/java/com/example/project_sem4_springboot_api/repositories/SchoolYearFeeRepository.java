package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.SchoolYearFee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolYearFeeRepository extends JpaRepository<SchoolYearFee,Long> {
    List<SchoolYearFee> findAllBySchoolyear_Id(Long schoolYearId);
    List<SchoolYearFee> findAllByIdIn(List<Long> ids);
}
