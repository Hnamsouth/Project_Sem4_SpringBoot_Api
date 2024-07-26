package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.dto.StudentYearHomeWorkDto;
import com.example.project_sem4_springboot_api.dto.TeacherContactDetail;
import com.example.project_sem4_springboot_api.entities.SchoolYearSubject;
import com.example.project_sem4_springboot_api.entities.Subject;
import com.example.project_sem4_springboot_api.entities.response.TeacherClassSubject;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class HomeWorkDto {
        private Long id;
        private String title;
        private String content;
        private Date dueDate;
        private Date startDate;
        private String description;
        private String url;
        private boolean status;
        private String statusName;
        private boolean overdue;
        private List<String> homeworkImageUrls;
        private List<StudentYearHomeWorkDto> studentYearHomeWorks;
        private boolean submission;
        private Subject subject;
        private Map<String,Object> teacherInfo;
}
