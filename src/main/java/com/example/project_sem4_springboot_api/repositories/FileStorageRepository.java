package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileStorageRepository extends JpaRepository<FileStorage,Long> {
    List<FileStorage> findAllByTags(String tags);
    List<FileStorage> findAllByTagsIn(List<String> tags);
    void deleteAllByTags(String tags);
}
