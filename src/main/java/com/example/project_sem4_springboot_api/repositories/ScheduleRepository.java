package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Schedule;
import com.example.project_sem4_springboot_api.entities.SchoolYearClass;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Subselect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {



    List<Schedule> findAllBySchoolYearClass(SchoolYearClass schoolYearClass);
}
