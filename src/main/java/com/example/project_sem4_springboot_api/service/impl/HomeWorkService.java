package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.StudentYearHomeWorkDto;
import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.request.HomeWorkDto;
import com.example.project_sem4_springboot_api.exception.ArgumentNotValidException;
import com.example.project_sem4_springboot_api.repositories.*;
import com.example.project_sem4_springboot_api.service.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class HomeWorkService {

    private final String HOMEWORK_FN= "homeWork";
    private final String HOMEWORK_TAG= "homework";
    private final String STUDENT_HOMEWORK_FN= "studentHomeWork";
    private final String STUDENT_HOMEWORK_TAG= "student-homework-";

    private final HomeWorkRepository homeWorkRepository;
    private final CloudinaryService cloudinaryService;
    private final TeacherSchoolYearClassSubjectRepository teacherSchoolYearClassSubjectRepository;
    private final StudentYearInfoRepository studentYearInfoRepository;
    private final StudentYearHomeWorkRepository studentYearHomeWorkRepository;

    public HomeWorkService(HomeWorkRepository homeWorkRepository, CloudinaryService cloudinaryService, TeacherSchoolYearClassSubjectRepository teacherSchoolYearClassSubjectRepository, StudentYearInfoRepository studentYearInfoRepository, StudentYearHomeWorkRepository studentYearHomeWorkRepository) {
        this.homeWorkRepository = homeWorkRepository;
        this.cloudinaryService = cloudinaryService;
        this.teacherSchoolYearClassSubjectRepository = teacherSchoolYearClassSubjectRepository;

        this.studentYearInfoRepository = studentYearInfoRepository;
        this.studentYearHomeWorkRepository = studentYearHomeWorkRepository;
    }

    public HomeWork createHomeWork(String title,
                                   String content,
                                   String dueDate,
                                   Long teacherSchoolYearClassSubjectId,
                                   List<MultipartFile> images) throws ParseException, IOException {
        TeacherSchoolYearClassSubject teacherSchoolYearClassSubject = teacherSchoolYearClassSubjectRepository.findById(teacherSchoolYearClassSubjectId)
                .orElseThrow(() -> new RuntimeException("TeacherSchoolYearClassSubject not found"));

        HomeWork homeWork = new HomeWork();
        homeWork.setTitle(title);
        homeWork.setContent(content);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        homeWork.setDueDate(dateFormat.parse(dueDate));
        homeWork.setTeacherSchoolYearClassSubject(teacherSchoolYearClassSubject);
        homeWork.setStatus(true);
        homeWork.setStatusName("Đang hoạt động");
        var saveHomeWork = homeWorkRepository.save(homeWork);

        cloudinaryService.uploadMultiImage(images,HOMEWORK_TAG + saveHomeWork.getId(), HOMEWORK_FN);

        saveHomeWork.setUrl("homework" + saveHomeWork.getId());

        return homeWorkRepository.save(saveHomeWork);
    }

    public StudentYearHomeWork submitHomeWork(List<MultipartFile> images, String description, Long studentYearInfoId, Long homeWorkId) throws Exception {
        HomeWork homeWork = homeWorkRepository.findById(homeWorkId)
                .orElseThrow(() -> new RuntimeException("HomeWork khong ton tai"));

        if (homeWork.getDueDate().before(new Date())) {
            throw new RuntimeException("bai tap dahet han");
        }

        StudentYearInfo studentYearInfo = studentYearInfoRepository.findById(studentYearInfoId)
                .orElseThrow(() -> new RuntimeException("Student Year Info khong ton tai"));
        //check nop chua

        Optional<StudentYearHomeWork> existingHomeWork =
                homeWork.getStudentYearHomeWorks().stream().filter(h->h.getStudentYearInfo().equals(studentYearInfo)).findFirst();

        StudentYearHomeWork studentYearHomeWork;
        if (existingHomeWork.isPresent()){
            //nop roi thi update
            var s = existingHomeWork.get();
            s.setDescription(description);
            s.setSubmitTime(new Date());
            // remove img tren cloudinary
            try {
                cloudinaryService.removeFileByTag(s.getUrl(),STUDENT_HOMEWORK_FN);
            }catch (Exception e){
                throw new ArgumentNotValidException("","","");
            }
            studentYearHomeWork = s;
        }else {
            studentYearHomeWork =  StudentYearHomeWork.builder()
                .description(description).submitTime(new Date()).status(true)
                    .point(0.0).studentYearInfo(studentYearInfo).homeWork(homeWork)
                .build();
        }
        // save StudentYearHomeWork
        var saveStudentYearHomeWork = studentYearHomeWorkRepository.save(studentYearHomeWork);
        // upload file to cloudinary
        cloudinaryService.uploadMultiImage(images,STUDENT_HOMEWORK_TAG + studentYearHomeWork.getId(), STUDENT_HOMEWORK_FN);

        saveStudentYearHomeWork.setUrl(STUDENT_HOMEWORK_TAG + studentYearHomeWork.getId());

        return studentYearHomeWorkRepository.save(saveStudentYearHomeWork);
    }

    public List<HomeWorkDto> getHomeWorksByStudentYearInfoId(Long studentYearInfoId) {
        var student = studentYearInfoRepository.findById(studentYearInfoId).orElseThrow(() ->
                new ArgumentNotValidException("Student not found", "", ""));

        List<HomeWork> homeWorks = homeWorkRepository.
                findAllByTeacherSchoolYearClassSubject_SchoolYearClass_Id(student.getSchoolYearClass().getId());

        return homeWorks.stream().map(s->{
            var students = s.convertToDto(studentYearInfoId);
            if(students.isSubmission()){
               var stdHw =  students.getStudentYearHomeWorks().get(0);
               stdHw.setImageUrl(cloudinaryService.getImageUrlByTag(stdHw.getUrl(),STUDENT_HOMEWORK_FN));
                students.setStudentYearHomeWorks(List.of(stdHw));
            }
            var homeWorkImageUrl = cloudinaryService.getImageUrlByTag(s.getUrl(),HOMEWORK_FN);
            students.setHomeworkImageUrls(homeWorkImageUrl);
            return students;
        }).toList();
    }

    public HomeWorkDto getHomeWorkDetail(Long homeWorkId) {
        HomeWork homeWork = homeWorkRepository.findById(homeWorkId)
                .orElseThrow(() -> new RuntimeException("HomeWork không tồn tại"));

        List<StudentYearHomeWork> studentHomeWorks = studentYearHomeWorkRepository.findByHomeWorkId(homeWorkId);
//        var listTags = studentHomeWorks.stream().map(StudentYearHomeWork::getUrl).toList();
//        var listImagesUrl = cloudinaryService.getImageUrlByTags(listTags,STUDENT_HOMEWORK_FN);
        List<StudentYearHomeWorkDto> studentHomeWorkDtos = studentHomeWorks.stream()
                .map(s->{
                    var student = s.convertToDto();
                    student.setImageUrl(cloudinaryService.getImageUrlByTag(s.getUrl(),STUDENT_HOMEWORK_FN));
                    return student;
                })
                .collect(Collectors.toList());

        var res = homeWork.convertToDtoOnlyHw();
        res.setStudentYearHomeWorks(studentHomeWorkDtos);
        res.setHomeworkImageUrls(cloudinaryService.getImageUrlByTag(homeWork.getUrl(),HOMEWORK_FN));

        return res;
    }

    private StudentYearHomeWorkDto convertToDto(StudentYearHomeWork studentYearHomeWork) {
        return StudentYearHomeWorkDto.builder()
                .id(studentYearHomeWork.getId())
                .description(studentYearHomeWork.getDescription())
                .url(studentYearHomeWork.getUrl())
                .submitTime(studentYearHomeWork.getSubmitTime())
                .status(studentYearHomeWork.isStatus())
                .statusName(studentYearHomeWork.getStatusName())
                .point(studentYearHomeWork.getPoint())
                .build();
    }

    /*
         4: lấy ds bài tập đã giao của giáo viên theo id phân công giảng dạy (techer_schoolyear_class_subject)
        DB(HomeWork)  , method: GET , params: techer_schoolyear_class_subject_id

         return : trả về danh sách bài tập + (số lượng hs đã đã nộp / sl hs của lớp )
         trả về ds url ảnh bài tập và ảnh bài nộp nếu có
    */
    public ResponseEntity<?> getHomeWorksByTeacher(Long teacherSchoolYearClassSubjectId){
        List<HomeWork> homeWorks = homeWorkRepository.findAllByTeacherSchoolYearClassSubjectId(teacherSchoolYearClassSubjectId);

        var res = homeWorks.stream().map(h->{
            var hw = h.toTeacherRes();
            hw.put("homeworkImageUrls",cloudinaryService.getImageUrlByTag(h.getUrl(),HOMEWORK_FN));
            return hw;
        }).toList();
        return ResponseEntity.ok(res);
    }



//    public List<HomeWorkDto> getHomeWorksByTeacherSchoolYearClassSubjectId(Long teacherSchoolYearClassSubjectId) {
//        List<HomeWork> homeWorks = homeWorkRepository.findByTeacherSchoolYearClassSubjectId(teacherSchoolYearClassSubjectId);
//        List<StudentYearHomeWork> studentYearHomeWorks = studentYearHomeWorkRepository.findByTeacherSchoolYearClassSubjectId(teacherSchoolYearClassSubjectId);
//
//        List<HomeWorkDto> homeWorkDTOs = new ArrayList<>();
//        for (HomeWork homeWork : homeWorks) {
//            HomeWorkDto homeWorkDto = HomeWorkDto.builder()
//                    .id(homeWork.getId())
//                    .title(homeWork.getTitle())
//                    .content(homeWork.getContent())
//                    .url(homeWork.getUrl())
//                    .dueDate(homeWork.getDueDate())
//                    .status(homeWork.isStatus())
//                    .statusName(homeWork.getStatusName())
//                    .teacherSchoolYearClassSubjectId(homeWork.getTeacherSchoolYearClassSubject())
//                    .homeworkImageUrls(Collections.singletonList(homeWork.getUrl()))
//                    .build();
//
//            long submittedStudents = studentYearHomeWorks.stream()
//                    .filter(s -> s.getHomeWork().getId().equals(homeWork.getId()))
//                    .count();
//            homeWorkDto.setSubmittedStudents((int) submittedStudents);
//
//            List<StudentYearHomeWorkDto> studentYearHomeWorkDtos = studentYearHomeWorks.stream()
//                    .filter(s -> s.getHomeWork().getId().equals(homeWork.getId()))
//                    .map(s -> StudentYearHomeWorkDto.builder()
//                            .id(s.getId())
//                            .description(s.getDescription())
//                            .status(s.isStatus())
//                            .statusName(s.getStatusName())
//                            .url(s.getUrl())
//                            .build())
//                    .collect(Collectors.toList());
//            homeWorkDto.setStudentYearHomeWorks(studentYearHomeWorkDtos);
//
//            int totalStudents = teacherSchoolYearClassSubjectRepository.findTotalStudentsByTeacherSchoolYearClassSubjectId(homeWork.getTeacherSchoolYearClassSubject().getId());
//            homeWorkDto.setTotalStudents(totalStudents);
//
//            homeWorkDTOs.add(homeWorkDto);
//        }
//
//        return homeWorkDTOs;
//    }



}















