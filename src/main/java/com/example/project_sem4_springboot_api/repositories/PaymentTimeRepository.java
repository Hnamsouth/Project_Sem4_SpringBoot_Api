package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.PaymentTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTimeRepository extends JpaRepository<PaymentTime,Long> {
}
