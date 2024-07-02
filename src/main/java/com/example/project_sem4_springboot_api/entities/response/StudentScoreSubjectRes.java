package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.ESem;
import lombok.Data;


@Data
public class StudentScoreSubjectRes extends StudentScoreSubject {
    private ESem semesterName;
    private String semester;
    private String status;
}
