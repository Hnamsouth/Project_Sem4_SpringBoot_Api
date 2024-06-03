package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.enums.AttendanceStatus;
import com.example.project_sem4_springboot_api.entities.enums.EStatus;
import com.example.project_sem4_springboot_api.entities.response.AttendanceRes;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendanceStatus;
    private String attendanceStatusName;
    private String notificationStatus;
    private String note;
    private Date createdAt;
    @ManyToOne
    @JoinColumn(name = "student_year_info_id")
    @JsonManagedReference
    private StudentYearInfo studentYearInfo;


    @JsonIgnore
    public AttendanceRes toRes(){
        return AttendanceRes.builder()
                .id(this.id)
                .attendanceStatus(this.attendanceStatus)
                .attendanceStatusName(this.attendanceStatusName)
                .notificationStatus(this.notificationStatus)
                .note(this.note)
                .createdAt(this.createdAt)
                .studentInfo(studentYearInfo.toStudentResponse())
                .build();
    }

}
