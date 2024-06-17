package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.FeePeriodScope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeePeriodScopeRepository extends JpaRepository<FeePeriodScope,Long> {
    List<FeePeriodScope> findAllByIdIn(List<Long> listIds);
}
