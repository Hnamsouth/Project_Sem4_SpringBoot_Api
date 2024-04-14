package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.CalendarRelease;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarReleaseRepository extends JpaRepository<CalendarRelease,Long> {

    List<CalendarRelease> findAllBySchoolYear_Id(Long SchoolYear_Id);
}
