package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.TakeLeave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TakeLeaveRepository extends JpaRepository<TakeLeave,Long> {
    List<TakeLeave> findAllByParent_IdOrStudentYearInfo_Id(Long parent_id, Long studentYearInfo_id);
}
