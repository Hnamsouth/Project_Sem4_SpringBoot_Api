package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageData,Integer> {
    List<ImageData> findByOrderById();
}
