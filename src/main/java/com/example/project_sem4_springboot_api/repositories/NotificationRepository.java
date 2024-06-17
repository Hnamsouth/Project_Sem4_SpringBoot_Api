package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<UserNotification,Long> {
}
