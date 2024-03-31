package com.example.project_sem4_springboot_api.service;

import com.example.project_sem4_springboot_api.dto.TeacherDetailsDto;
import com.example.project_sem4_springboot_api.dto.TeacherDto;
import com.example.project_sem4_springboot_api.dto.UserDto;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.entities.Teacher;
import com.example.project_sem4_springboot_api.exception.ResourceNotFoundException;

import java.util.List;

public interface TeacherService {

    Teacher createTeacher(TeacherDetailsDto teacherDetailsDto, Long userId) throws ResourceNotFoundException;

    List<TeacherDto> getAllTeacher();

    Teacher updateTeacher(TeacherDetailsDto teacherDetailsDto, Long teacherId) throws ResourceNotFoundException;

    Teacher findTeacherById(Long teacherId)throws Exception;

    boolean deleteTeacher(Long id);

}
