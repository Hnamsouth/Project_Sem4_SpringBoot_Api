package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.TransactionDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail,Long> {
}
