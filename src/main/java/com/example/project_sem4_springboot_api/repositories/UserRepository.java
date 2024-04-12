package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface UserRepository  extends JpaRepository<User,Long> {

    boolean existsByUsername(String username);

    boolean existsById(Long id);

    @Override
    Optional<User> findById(Long id);


    Optional<User> findByUsername(String username);


    List<User> findAllByUsernameContains(String username);
}