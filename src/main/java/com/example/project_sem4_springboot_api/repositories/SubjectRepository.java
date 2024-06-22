package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Query("SELECT s FROM Subject s WHERE s.id = :id or s.id = :id-1")
    List<Subject> findAllById(@Param("id") Long id);
    List<Subject> findAllByIdIn(List<Long> ids);


    @Query("SELECT s FROM Subject s WHERE s.id in :ids")
    List<Subject> findAllByIdInSubject(@Param("ids")List<Long> ids);




}
