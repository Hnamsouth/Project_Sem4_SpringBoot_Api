package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.TeacherSchoolYear;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Builder
@Data
public class TSYCSResponse{
    public TeacherSchoolYear teacherSchoolYear;
    public List<SubjectClassRes> subjectClassResList;
}
