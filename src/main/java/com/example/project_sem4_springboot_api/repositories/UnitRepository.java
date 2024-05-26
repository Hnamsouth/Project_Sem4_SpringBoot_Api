package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit,Long> {
    List<Unit> findAllByIdIn(List<Long> listUnitId);
}
