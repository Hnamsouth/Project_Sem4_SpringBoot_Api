package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.enums.ESem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class StudyResultRes {
    private ESem semester;
    private String semesterName;
    private List<ClassInfo> classList;
}
