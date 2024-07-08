package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.dto.StudentYearHomeWorkDto;
import com.example.project_sem4_springboot_api.dto.TeacherContactDetail;
import com.example.project_sem4_springboot_api.entities.response.TeacherClassSubject;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class HomeWorkDto {
        private Long id;
        private String title;
        private String content;
        private Date dueDate;
        private String description;
        private String url;
        private boolean status;
        private String statusName;
        private boolean overdue;
        private List<String> homeworkImageUrls;
        private List<String> studentHomeworkImageUrls;
        private List<StudentYearHomeWorkDto> studentYearHomeWorks;
        private boolean studentHomeWorkStatus;
        private boolean submission;
        private TeacherClassSubject teacherContactDetail;
}
