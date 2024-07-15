package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.UserDeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface UserDeviceTokenRepository extends JpaRepository<UserDeviceToken,Long> {

    Optional<UserDeviceToken>  findByDeviceTokenAndOsAndUser_Id(String deviceToken, String os, Long user_id);

    List<UserDeviceToken> findByUser_Id(Long userId);
}