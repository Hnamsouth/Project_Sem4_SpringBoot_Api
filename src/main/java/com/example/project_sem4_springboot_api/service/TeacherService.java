package com.example.project_sem4_springboot_api.service;

import com.example.project_sem4_springboot_api.dto.TeacherDto;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.entities.Teacher;

import java.util.List;

public interface TeacherService {

    TeacherDto createTeacherDto(TeacherDto teacherDto);

    List<TeacherDto> getAllTeacher();

    Teacher updateTeacher(Teacher teacher, Long teacherId)throws Exception;

    Teacher findTeacherById(Long teacherId)throws Exception;

    boolean deleteTeacher(Long id);

}
