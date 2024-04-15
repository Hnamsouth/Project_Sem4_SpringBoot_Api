package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.TeacherContactDetail;
import com.example.project_sem4_springboot_api.dto.TeacherDetailsDto;
import com.example.project_sem4_springboot_api.dto.TeacherDto;
import com.example.project_sem4_springboot_api.entities.Teacher;
import com.example.project_sem4_springboot_api.entities.TeacherSchoolYearClassSubject;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.entities.UserDetail;
import com.example.project_sem4_springboot_api.entities.enums.TeacherType;
import com.example.project_sem4_springboot_api.exception.ResourceNotFoundException;
import com.example.project_sem4_springboot_api.repositories.*;
import com.example.project_sem4_springboot_api.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final UserDetailRepository userDetailRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;
    private final TeacherSchoolYearClassSubjectRepository teacherSchoolYearClassSubjectRepository;
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
            throw new RuntimeException("Error creating teacher: " + e.getMessage());
        }
    }

    @Override
    public List<TeacherDto> getAllTeacher() {
        List<Teacher> teacherDto = teacherRepository.findAll();
        return null;
//        return teacherDto.stream().map(Teacher::getDto).collect(Collectors.toList());
    }

    public ResponseEntity<?> getTeacher(Long id){
        if(id!=null){
            return  ResponseEntity.ok(teacherRepository.findById(id).orElseThrow(
                    ()->new NullPointerException("Giao vien khong ton tai")
            ));
        }
        return ResponseEntity.ok(teacherRepository.findAll());
    }


    public Teacher updateTeacher(TeacherDetailsDto teacherDetailsDto, Long teacherId) throws ResourceNotFoundException {
        try {
            Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + teacherId));

            teacher.setOfficerNumber(teacherDetailsDto.getOfficerNumber());
            teacher.setPositionId(teacherDetailsDto.getPositionId());
            teacher.setJoiningDate(teacherDetailsDto.getJoiningDate());

            UserDetail userDetails = teacher.getUser().getUserDetail().get(0);
            if (userDetails == null) {
                throw new ResourceNotFoundException("User details not found for teacher: " + teacherId);
            }

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

    /**
     * get tất cả gv dạy của lớp theo schoolYearClassId
     *
     **/
    public ResponseEntity<?> getContactTeacher(Long schoolYearClassId){
        var result =  teacherSchoolYearClassSubjectRepository.findAllBySchoolYearClass_Id(schoolYearClassId);
        if(result.isEmpty()) throw new NullPointerException("Khong tim thay giao vien nao");
        // những gv dạy 2 môn trở lên sẽ cập nhật subjects
        List<TeacherContactDetail> teacherContactDetails = new ArrayList<>();
        for(TeacherSchoolYearClassSubject e : result){
            Long teacherSchoolYearId = e.getTeacherSchoolYear().getId();
            String subject = e.getSchoolYearSubject().getSubject().getName();
            var checkExist = teacherContactDetails.stream().filter((s)->s.getTeacherSchoolYearId().equals(teacherSchoolYearId)).toList();
            if(!checkExist.isEmpty()){
                checkExist.forEach(s->teacherContactDetails.set(teacherContactDetails.indexOf(s),s.addSubject(subject)));
                continue;
            }
            teacherContactDetails.add(e.toContact());
        }
        return ResponseEntity.ok(teacherContactDetails);
    }



}
