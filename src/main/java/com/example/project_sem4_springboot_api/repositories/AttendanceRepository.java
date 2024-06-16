package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findAllByStudentYearInfo_Id(Long studentYearInfoId);
    @Query("select a from Attendance a where a.studentYearInfo.id = :schoolYearClass_id and DATE(:dayOff)  = DATE(a.createdAt)")
    List<Attendance> getAttendanceClassWithDayOff(@Param("schoolYearClass_id") Long schoolYearClass_id,@Param("dayOff") Date dayOff);
}
