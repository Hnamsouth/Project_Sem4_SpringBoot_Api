package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User,Long> {

    boolean existsByUsername(String username);

    boolean existsById(Long id);

    @Override
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);
}