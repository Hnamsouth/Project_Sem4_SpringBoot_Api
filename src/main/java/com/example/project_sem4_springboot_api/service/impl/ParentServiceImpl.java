package com.example.project_sem4_springboot_api.service.impl;


import com.example.project_sem4_springboot_api.entities.Parent;
import com.example.project_sem4_springboot_api.entities.request.CreateParent;
import com.example.project_sem4_springboot_api.repositories.ParentRepository;
import com.example.project_sem4_springboot_api.repositories.SchoolYearClassRepository;
import com.example.project_sem4_springboot_api.repositories.TeacherSchoolYearRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@RequiredArgsConstructor
public class ParentServiceImpl {

    private  final ParentRepository parentRepository;
    private  final TeacherSchoolYearRepository teacherSchoolYearRepository;
    private  final SchoolYearClassRepository schoolYearClassRepository;


    public ResponseEntity<?> createParent(CreateParent createParent){


         parentRepository.save(Parent.builder()
                .fullName(createParent.getFullName())
                .phone(createParent.getPhone())
                .address(createParent.getAddress())
                .email(createParent.getEmail())
                .gender(createParent.isGender())
//                .user()
                .build());
        return null;
    }
}
