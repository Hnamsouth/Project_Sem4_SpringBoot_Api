package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.UserNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface UserNotificationRepository extends JpaRepository<UserNotification,Long> {
    // get UserNotification by user id and order by createdAt desc and paging
    @Query("select un from UserNotification un where un.user.id = :userId order by un.createdAt desc ")
    Page<UserNotification> findUserNotifications(@Param("userId") Long userId, Pageable pageable);

    List<UserNotification> findAllByUserIdOrderByCreatedAtDesc(Long user_id);

    Optional<UserNotification> findByIdAndUserId(Long id, Long userId);
}