package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.dto.TeacherContactDetail;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TeacherClassSubject {
    public Long id;
    public TeacherContactDetail teacher;
    public SubjectRes subject;
}
