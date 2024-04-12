package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.enums.DayOfWeek;
import com.example.project_sem4_springboot_api.entities.enums.StudyTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import java.util.Date;


@Data
@Builder
public class ScheduleResponse {
    private Long id;
    private int indexLesson;
    @Enumerated(EnumType.STRING)
    private StudyTime studyTime;
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;
    private Date releaseAt;
    private String note;
    // id, sort name
    @JsonProperty("teacherId")
    private Long teacherSchoolYearId;
    @JsonProperty("teacherName")
    private String teacherSchoolYearName;
    // id , name
    @JsonProperty("classId")
    private Long schoolYearClassId;
    @JsonProperty("className")
    private String SchoolYearClassName;
    // id name
    @JsonProperty("subjectId")
    private Long schoolYearSubjectId;
    @JsonProperty("subjectName")
    private String SchoolYearSubjectName;
}
