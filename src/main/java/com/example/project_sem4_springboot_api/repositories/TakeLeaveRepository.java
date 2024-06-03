package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.TakeLeave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TakeLeaveRepository extends JpaRepository<TakeLeave,Long> {
}
