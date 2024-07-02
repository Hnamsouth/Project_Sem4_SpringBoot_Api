package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.SchoolYearSubject;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class InsertPoints {
    private SchoolYearSubject schoolYearSubject;
    private List<ClassInfo> classList;
}


