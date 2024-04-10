package com.example.project_sem4_springboot_api.repositories;

import com.example.project_sem4_springboot_api.entities.SchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SchoolYearRepository extends JpaRepository<SchoolYear, Long> {

    @Query("SELECT YEAR(s.startSem1) FROM SchoolYear s ")
    List<Integer> getAllYear();



    @Query("SELECT sy FROM SchoolYear sy" +
            " WHERE (:id is NULL or sy.id = :id )" +
            "AND (:startSem1 is NULL or sy.startSem1 = :startSem1) " +
            "AND (:startSem2 is NULL or sy.startSem2 = :startSem2 )" +
            "AND (:end is NULL or sy.end = :end) " )
    Optional<SchoolYear> filterSchoolYear(
            @Param("id") Long id,
            @Param("startSem1") Date startSem1,
            @Param("startSem2") Date startSem2,
            @Param("end") Date end);
}
