package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.constants.StatusData;
import com.example.project_sem4_springboot_api.dto.StudentParentCreateDto;
import com.example.project_sem4_springboot_api.entities.Role;
import com.example.project_sem4_springboot_api.entities.Status;
import com.example.project_sem4_springboot_api.entities.Student;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.exception.DataExistedException;
import com.example.project_sem4_springboot_api.repositories.*;
import jakarta.annotation.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StudentParentServiceImpl {

    UserDetailRepository userDetailRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    StatusRepository statusRepository;
    StudentRepository studentRepository;

    public ResponseEntity<?> createStudentParent(StudentParentCreateDto data){
        var findStudent= new HashSet<>(studentRepository.findAllByIdIn(data.getStudentList()));//lay danh sach student
        if (findStudent.isEmpty())throw new  NullPointerException("Student khong ton tai");
        Set<Role> roles = roleRepository.findByIdIn(data.getRole());
        if (roles.isEmpty()) throw new NullPointerException("Role khong ton tai");
        if (userRepository.existsByUsername(data.getUsername())) throw new DataExistedException("Username da ton tai");
        Status status = statusRepository.findByCode(StatusData.CREATE_NEW_USER);

        var user = data.toUser(roles, passwordEncoder.encode(data.getPassword()), status);
        user.setStudents(findStudent.stream().toList());

        var newParent = userRepository.save(user);
        var parentDetail = userDetailRepository.save(data.toUserDetail(newParent));
        newParent.setUserDetail(List.of(parentDetail));

        return new  ResponseEntity<>(parentDetail, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getParent(@Nullable Long id){
        if (id!=null) return ResponseEntity.ok(userRepository.findById(id).orElseThrow(()->new NullPointerException("Id User khong ton tai!")));
        return ResponseEntity.ok(userRepository.findAll().stream().map(User::getDto).collect(Collectors.toList()));
    }

//    public ResponseEntity<?> updateParent()

    public ResponseEntity<?> deleteParent(@Nullable Long id){
        User user = userRepository.findById(id).orElseThrow(()->new NullPointerException("Id Parent không tồn tại!!!"));
        userRepository.deleteById(id);
        return ResponseEntity.ok("Xoa thanh cong Parent." + user.getUsername());
    }



}
