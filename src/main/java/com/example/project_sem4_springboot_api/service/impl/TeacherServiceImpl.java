package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.TeacherDetailsDto;
import com.example.project_sem4_springboot_api.dto.TeacherDto;
import com.example.project_sem4_springboot_api.dto.UserDto;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.entities.Teacher;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.entities.UserDetail;
import com.example.project_sem4_springboot_api.exception.ResourceNotFoundException;
import com.example.project_sem4_springboot_api.repositories.TeacherRepository;
import com.example.project_sem4_springboot_api.repositories.UserDetailRepository;
import com.example.project_sem4_springboot_api.repositories.UserRepository;
import com.example.project_sem4_springboot_api.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final UserDetailRepository userDetailRepository;

    private final TeacherRepository teacherRepository;

    private final UserRepository userRepository;

    public Teacher createTeacher(TeacherDetailsDto teacherDetailsDto, Long userId) throws ResourceNotFoundException {
        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

            Teacher teacher = new Teacher();
            teacher.setUser(user);
            teacher.setOfficerNumber(teacherDetailsDto.getOfficerNumber());
            teacher.setPositionId(teacherDetailsDto.getPositionId());
            teacher.setJoiningDate(teacherDetailsDto.getJoiningDate());
            teacher.setActive(teacher.isActive());

            UserDetail userDetails = new UserDetail();
            userDetails.setUser(user);
            userDetails.setFirstname(teacherDetailsDto.getFirstname());
            userDetails.setLastname(teacherDetailsDto.getLastname());
            userDetails.setAddress(teacherDetailsDto.getAddress());
            userDetails.setPhone(teacherDetailsDto.getPhone());
            userDetails.setEmail(teacherDetailsDto.getEmail());
            userDetails.setGender(teacherDetailsDto.isGender());
            userDetails.setBirthday(teacherDetailsDto.getBirthday());

            teacherRepository.save(teacher);

            return teacher;
        } catch (Exception e) {
            // Xử lý lỗi chung khi lưu Teacher
            throw new RuntimeException("Error creating teacher: " + e.getMessage());
        }
    }

    public List<TeacherDto> getAllTeacher(){
        List<Teacher> teacherDto = teacherRepository.findAll();
        return teacherDto.stream().map(Teacher::getDto).collect(Collectors.toList());
    }


    public Teacher updateTeacher(TeacherDetailsDto teacherDetailsDto, Long teacherId) throws ResourceNotFoundException {
        try {
            Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));

            // Cập nhật thông tin Teacher
            teacher.setOfficerNumber(teacherDetailsDto.getOfficerNumber());
            teacher.setPositionId(teacherDetailsDto.getPositionId());
            teacher.setJoiningDate(teacherDetailsDto.getJoiningDate());

            UserDetail userDetails = teacher.getUser().getUserDetaill();
            if (userDetails == null) {
                throw new ResourceNotFoundException("User details not found for teacher: " + teacherId);
            }

            // Cập nhật thông tin UserDetails
            userDetails.setFirstname(teacherDetailsDto.getFirstname());
            userDetails.setLastname(teacherDetailsDto.getLastname());
            userDetails.setAddress(teacherDetailsDto.getAddress());
            userDetails.setPhone(teacherDetailsDto.getPhone());
            userDetails.setEmail(teacherDetailsDto.getEmail());
            userDetails.setGender(teacherDetailsDto.isGender()); // Giới tính boolean
            userDetails.setBirthday(teacherDetailsDto.getBirthday());
            // Set other user details fields based on teacherDetailsDto

            // Lưu Teacher (cascade update)
            teacherRepository.save(teacher);

            return teacher;
        } catch (Exception e) {
            // Xử lý lỗi chung khi lưu Teacher
            throw new RuntimeException("Error updating teacher: " + e.getMessage());
        }
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
