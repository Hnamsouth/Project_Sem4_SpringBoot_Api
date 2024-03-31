package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
}
