package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.TeacherContactDetail;
import com.example.project_sem4_springboot_api.dto.TeacherDetailsDto;
import com.example.project_sem4_springboot_api.dto.TeacherUpdateDto;
import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.exception.DataExistedException;
import com.example.project_sem4_springboot_api.repositories.*;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl {

    private final UserDetailRepository userDetailRepository;
    private final TeacherRepository teacherRepository;
    private final TeacherSchoolYearClassSubjectRepository teacherSchoolYearClassSubjectRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public ResponseEntity<?> createTeacher(TeacherDetailsDto data) {

        Set<Role> roles = roleRepository.findByIdIn(data.getRole());
        if(roles.isEmpty()) throw new NullPointerException("Role không tồn tại !!!");
        if(userRepository.existsByUsername(data.getUsername())) throw new DataExistedException("Username đã tồn tại!!!");

        var newUser = userRepository.save(data.toUser(roles,passwordEncoder.encode(data.getPassword())));
        var userDetail = userDetailRepository.save(data.toUserDetail(newUser));
        newUser.setUserDetail(List.of(userDetail));
        var newTeacher =teacherRepository.save(data.toTeacher(newUser));

        return new ResponseEntity<>(newTeacher.toResponse(), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getTeacher(@Nullable  boolean status,@Nullable Long id) {
        if(id!=null) return ResponseEntity.ok(teacherRepository.findById(id).orElseThrow(()->new NullPointerException("Id Giáo viên không tồn tại!!!")).toResponse());
        if(status) return ResponseEntity.ok(teacherRepository.findAllByActive(false).stream().map(Teacher::toResponse).toList());
        return ResponseEntity.ok(teacherRepository.findAll().stream().map(Teacher::toResponse).toList());
    }

    public ResponseEntity<?> updateTeacher(TeacherUpdateDto data){
        try{
            Teacher teacher = teacherRepository.findById(data.getId()).orElseThrow(() -> new NullPointerException("Không tìm thấy Giáo viên với id: " + data.getId()));
            Set<Role> roles = roleRepository.findByIdIn(data.getRole());
            if(roles.isEmpty()) throw new NullPointerException("Role không tồn tại !!!");
            User user = teacher.getUser();
            if(!data.getUsername().equals(user.getUsername())) {
                if(userRepository.existsByUsername(data.getUsername())) throw new DataExistedException("Username đã tồn tại!!!");
                user.setUsername(data.getUsername());
            }
            if(!data.getPassword().equals(user.getRealPassword())){
                user.setPassword(passwordEncoder.encode(data.getPassword()));
                user.setRealPassword(data.getPassword());
            }
            user.setRoles(roles);
            var newUser = userRepository.save(user);

            UserDetail userDetail = newUser.getUserDetail().get(0).from(data);
            userDetailRepository.save(userDetail);

            var newTeacher = teacherRepository.save(teacher.from(data)).toResponse();

            return ResponseEntity.ok(newTeacher);
        }catch (Exception e){
            LoggerFactory.getLogger(TeacherServiceImpl.class).error("Lỗi cập nhật thông tin giáo viên: "+e.getMessage());
            throw new NullPointerException("Lỗi cập nhật thông tin giáo viên: "+e.getMessage());
        }
    }

    public ResponseEntity<?> deleteTeacher(Long id){
        Teacher teacher = teacherRepository.findById(id).orElseThrow(()->new NullPointerException("Id Gíao viên không tồn tại!!!"));
        teacherRepository.deleteById(id);
        return ResponseEntity.ok("Xóa thành công giáo viên: "+teacher.getSortName());
    }

    /**
     * get tất cả gv dạy của lớp theo schoolYearClassId
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
