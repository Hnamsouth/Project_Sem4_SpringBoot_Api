package com.example.project_sem4_springboot_api.entities.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetSchoolYearClass {
    private Long id;
    private Long gradeId;
    private Long schoolYearId;
    private Long teacherSchoolYearId;
    private Long roomId;
    private String className;
    private String classCode;
}
