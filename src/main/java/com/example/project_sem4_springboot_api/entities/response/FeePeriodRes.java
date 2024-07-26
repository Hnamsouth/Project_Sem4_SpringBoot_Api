package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.TeacherSchoolYearClassSubject;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Builder
@Data
public class FeePeriodRes {
    private Long id;
    private String title;
    private String content;
    private boolean status;
    private String statusCode;
    private String endDate;
    private String createdAt;
    private List<Map<String,Object>> classList;
    private int totalTrans;
    private int totalPaid;
}
