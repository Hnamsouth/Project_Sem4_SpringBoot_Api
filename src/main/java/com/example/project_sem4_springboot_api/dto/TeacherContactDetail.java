package com.example.project_sem4_springboot_api.dto;


import com.example.project_sem4_springboot_api.entities.enums.TeacherType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
public class TeacherContactDetail {
    public Long teacherSchoolYearId;
    public String name;
    public String email;
    public String phone;
    public Set<String> subjects;
    @Enumerated(EnumType.STRING)
    public TeacherType teacherType;

    @JsonIgnore
    public TeacherContactDetail addSubject(String subject){
        Set<String> s = subjects.stream().map(String::new).collect(Collectors.toSet());
        s.add(subject);
        this.setSubjects(s);
        return this;
    }

}
