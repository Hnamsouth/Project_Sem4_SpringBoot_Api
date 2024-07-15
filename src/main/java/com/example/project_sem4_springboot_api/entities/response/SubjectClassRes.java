package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.SchoolYearClass;
import com.example.project_sem4_springboot_api.entities.SchoolYearSubject;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
@Builder
@Data
public class SubjectClassRes {
    public Long teacherSchoolYearSubjectId;
    public SchoolYearSubject schoolYearSubject;
    public List<SchoolYearClass> schoolYearClassList;
}
