package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailRepository extends JpaRepository<UserDetail,Long> {
}
