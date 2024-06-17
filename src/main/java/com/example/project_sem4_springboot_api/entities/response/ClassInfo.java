package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.SchoolYearClass;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClassInfo {
    private int numberOfStudent;
    private int completedQuantity;
    private SchoolYearClass classInfo;
    private boolean isCompleted;
    private String completed;
}
