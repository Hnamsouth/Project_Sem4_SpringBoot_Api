package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findAllByStudentYearInfo_Id(Long studentYearInfoId);
}
