package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.TeacherDto;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.entities.Teacher;
import com.example.project_sem4_springboot_api.repositories.TeacherRepository;
import com.example.project_sem4_springboot_api.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherDto createTeacherDto(TeacherDto teacherDto){
        Teacher teacher = new Teacher();
        teacher.setActive(teacherDto.isActive());
        teacher.setOfficerNumber(teacherDto.getOfficerNumber());
        teacher.setJoiningDate(teacherDto.getJoiningDate());
        return teacherRepository.save(teacher).getDto();
    }

    public List<TeacherDto> getAllTeacher(){
        List<Teacher> teacherDto = teacherRepository.findAll();
        return teacherDto.stream().map(Teacher::getDto).collect(Collectors.toList());
    }

    @Override
    public Teacher updateTeacher(Teacher teacher, Long teacherId) throws Exception {
        Optional<Teacher> teacher1 = teacherRepository.findById(teacherId);
        if (teacher1.isEmpty()){
            throw new Exception("user not exits with id " + teacherId);
        }
        Teacher oldTeacher = teacher1.get();

        if (teacher.getOfficerNumber()!=null){
            oldTeacher.setOfficerNumber(teacher.getOfficerNumber());
        }
        if (teacher.getJoiningDate()!=null){
            oldTeacher.setJoiningDate(teacher.getJoiningDate());
        }
        Teacher updatedTeacher = teacherRepository.save(oldTeacher);
        return updatedTeacher;
    }

    @Override
    public Teacher findTeacherById(Long teacherId) throws Exception {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        if (teacher.isPresent()){
            return teacher.get();
        }
        throw new Exception("user not exits with user id " + teacherId);
    }

    public boolean deleteTeacher(Long id){
        Optional<Teacher> teacherOptional = teacherRepository.findById(id);
        if (teacherOptional.isPresent()){
            teacherRepository.deleteById(id);
            return true;
        }
        return false;
    }

}
