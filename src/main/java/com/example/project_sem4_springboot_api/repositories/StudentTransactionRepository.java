package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.StudentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentTransactionRepository extends JpaRepository<StudentTransaction,Long> {
    List<StudentTransaction> findAllByFeePeriod_Id(Long feePeriodId);
    StudentTransaction findByFeePeriod_IdAndStudentYearInfo_Id(Long feePeriodId, Long studentYearInfoId);
}
