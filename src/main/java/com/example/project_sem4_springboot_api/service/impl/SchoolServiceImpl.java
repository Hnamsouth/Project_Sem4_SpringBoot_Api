package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.SchoolYearClassDto;
import com.example.project_sem4_springboot_api.dto.SchoolYearDto;
import com.example.project_sem4_springboot_api.dto.SchoolYearSubjectDto;
import com.example.project_sem4_springboot_api.dto.TeacherSchoolYearDto;
import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.EGrade;
import com.example.project_sem4_springboot_api.repositories.*;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl {

    private final SchoolYearRepository schoolYearRepository;
    private final SchoolYearSubjectRepository schoolYearSubjectRepository;
    private final SchoolYearClassRepository schoolYearClassRepository;
    private final TeacherSchoolYearRepository teacherSchoolYearRepository;
    private final TeacherRepository teacherRepository;
    private final GradeRepository gradeRepository;
    private final SubjectRepository subjectRepository;
    private final RoomRepository roomRepository;
    /*
    * 1: create school year
    * 2: create schoolyear_subject
    * 3: create teacher_schoolyear
    * 4: create schoolyear_class
    * */

    public ResponseEntity<?> createSchoolYear(SchoolYearDto data){
        try {
            var createdData =  schoolYearRepository.save(
                SchoolYear.builder()
                    .startSem1(data.getStartSem1())
                    .startSem2(data.getStartSem2())
                    .end(data.getEnd())
                    .build()
            );
            return ResponseEntity.ok(Map.entry("Create School Year Success",createdData));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public ResponseEntity<?> createSchoolYearSubject(SchoolYearSubjectDto data){
        try {
            if(data.getSubject() != null){
                var createdData = schoolYearSubjectRepository.save(
                        SchoolYearSubject.builder()
                                .subject(subjectRepository.findById(data.getSubject()).orElseThrow(
                                        ()->new NullPointerException("Subject Invalid")
                                ))
                                .schoolYear(schoolYearRepository.findById(data.getSchoolYear()).orElseThrow(
                                        ()->new NullPointerException("Invalid School Year")
                                ))
                                .build()
                );
                return ResponseEntity.ok(Map.entry("Create School Year Subject Success",createdData));
            }
            if(!data.getSubjects().isEmpty()){
                var subjects = subjectRepository.findAllById(data.getSubjects());
                if(!subjects.isEmpty()){
                    var createdData =  schoolYearSubjectRepository.saveAll(
                            subjects.stream().map((s)-> SchoolYearSubject.builder()
                                    .subject(s)
                                    .schoolYear(schoolYearRepository.findById(data.getSchoolYear()).orElseThrow(
                                            ()->new NullPointerException("Invalid School Year")
                                    ))
                                    .build()).toList()
                    );
                    return ResponseEntity.ok(Map.entry("Create School Year Subject Success",createdData));
                }
                return ResponseEntity.badRequest().body("Subject List Invalid !!!");
            }
            return ResponseEntity.badRequest().body("Subject Empty !!!");

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public ResponseEntity<?> createTeacherSchoolYear(TeacherSchoolYearDto data){
        try {
            if(data.getTeacher() != null){
                var createdData = teacherSchoolYearRepository.save(
                    TeacherSchoolYear.builder()
                        .teacher(teacherRepository.findById(data.getTeacher()).orElseThrow(
                            ()->new NullPointerException("Invalid Teacher")
                        ))
                        .schoolYear(schoolYearRepository.findById(data.getSchoolYear()).orElseThrow(
                                ()->new NullPointerException("Invalid School Year")
                        ))
                        .build()
                );
                return ResponseEntity.ok(Map.entry("Create Teacher School Year Success",createdData));
            }
            if(!data.getTeachers().isEmpty()){
                var teachers = teacherRepository.findAllById(data.getTeachers());
                if(!teachers.isEmpty()){
                    var createdData =  teacherSchoolYearRepository.saveAll(
                        teachers.stream().map((t)-> TeacherSchoolYear.builder()
                            .teacher(t)
                                .schoolYear(schoolYearRepository.findById(data.getSchoolYear()).orElseThrow(
                                        ()->new NullPointerException("Invalid School Year")
                                ))
                            .build()).toList()
                    );
                    return ResponseEntity.ok(Map.entry("Create List Teacher School Year Success",createdData));
                }
                return ResponseEntity.badRequest().body("Teacher List Invalid !!!");
            }
            return ResponseEntity.badRequest().body("Teacher Empty !!!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public ResponseEntity<?> createSchoolYearClass(SchoolYearClassDto data){
        try{
            var createdData=  schoolYearClassRepository.save(
                SchoolYearClass.builder()
                    .className(data.getClassName())
                    .classCode(data.getClassCode())
                    .teacherSchoolYear(teacherSchoolYearRepository.findById(data.getTeacherSchoolYear()).orElseThrow(
                        ()->new NullPointerException("Invalid teacher !!!")
                    ))
                    .grade(gradeRepository.findById(data.getGrade()).orElseThrow(
                        ()->new NullPointerException("Invalid grade !!!")
                    ))
                    .room(roomRepository.findById(data.getRoom()).orElseThrow(
                        ()->new NullPointerException("Invalid room !!!")
                    ))
                    .schoolYear(schoolYearRepository.findById(data.getSchoolYear()).orElseThrow(
                        ()->new NullPointerException("Invalid school year !!!")
                    ))
                    .build()
            );
            return ResponseEntity.ok(Map.entry("Create School Year Class Success",createdData));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
    * 1: read subject
    *
    *
    * */
    public ResponseEntity<?> getSubject(@Nullable Long id){
        try {
            if(id!=null){
                return ResponseEntity.ok(subjectRepository.findById(id).orElseThrow(
                    ()->new NullPointerException("NotFound!!!")
                ));
            }
            return ResponseEntity.ok(subjectRepository.findAll());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public ResponseEntity<?> getSchoolYear(
            @Nullable Long id,
            @Nullable Date startSem1,
            @Nullable Date startSem2,
            @Nullable Date end
    ){
        try {
            if(id!=null || startSem1 != null|| startSem2 != null|| end != null){
                return ResponseEntity.ok(schoolYearRepository
                    .filterSchoolYear(id,startSem1,startSem2,end)
                    .orElseThrow(()->new NullPointerException("Not Found!!!")));
            }
            return ResponseEntity.ok(schoolYearRepository.findAll());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
