package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.StudentYearHomeWorkDto;
import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.request.HomeWorkDto;
import com.example.project_sem4_springboot_api.repositories.*;
import com.example.project_sem4_springboot_api.service.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class HomeWorkService {

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
                                   List<MultipartFile> images) throws ParseException {
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
        var saveHomeWork =  homeWorkRepository.save(homeWork);

        for (MultipartFile image : images) {
            String imageUrl = cloudinaryService.uploadImage(image, "homework" + saveHomeWork.getId(), "homeWork");

        }
        saveHomeWork.setUrl("homework" + saveHomeWork.getId());

        return homeWorkRepository.save(saveHomeWork);
    }

    public StudentYearHomeWork submitHomeWork(List<MultipartFile> images, String description, Long studentYearInfoId, Long homeWorkId) {
        HomeWork homeWork = homeWorkRepository.findById(homeWorkId)
                .orElseThrow(() -> new RuntimeException("HomeWork khong ton tai"));

        if (homeWork.getDueDate().before(new Date())) {
            throw new RuntimeException("bai tap dahet han");
        }

        StudentYearHomeWork studentYearHomeWork = new StudentYearHomeWork();
        studentYearHomeWork.setDescription(description);
        studentYearHomeWork.setSubmitTime(new Date());
        studentYearHomeWork.setStatus(true);
        studentYearHomeWork.setStatusName("Đã nộp");
        studentYearHomeWork.setPoint(0.0);
        studentYearHomeWork.setStudentYearInfo(studentYearInfoRepository.findById(studentYearInfoId)
                .orElseThrow(() -> new RuntimeException("StudentYearInfo notfound")));
        studentYearHomeWork.setHomeWork(homeWork);
        var saveStudentYearHomeWork = studentYearHomeWorkRepository.save(studentYearHomeWork);

        for (MultipartFile image : images) {
            String imageUrl = cloudinaryService.uploadImage(image, "student-homework-" + studentYearHomeWork.getId(), "studentHomeWork");
            studentYearHomeWork.setUrl(imageUrl);
        }

        return studentYearHomeWorkRepository.save(saveStudentYearHomeWork);
    }

    public List<HomeWorkDto> getHomeWorksByStudentYearInfoId(Long studentYearInfoId) {
        List<StudentYearHomeWork> studentYearHomeWorks = studentYearHomeWorkRepository.findByStudentYearInfoId(studentYearInfoId);
        List<HomeWork> homeWorks = new ArrayList<>();
        for (StudentYearHomeWork studentYearHomeWork : studentYearHomeWorks) {
            homeWorks.add(studentYearHomeWork.getHomeWork());
        }

        List<HomeWorkDto> homeWorkDTOs = new ArrayList<>();
        for (HomeWork homeWork : homeWorks) {
            HomeWorkDto homeWorkDTO = HomeWorkDto.builder()
                    .id(homeWork.getId())
                    .title(homeWork.getTitle())
                    .content(homeWork.getContent())
                    .dueDate(homeWork.getDueDate())
                    .url(homeWork.getUrl())
                    .status(homeWork.isStatus())
                    .statusName(homeWork.getStatusName())
                    .studentYearHomeWorks(getStudentYearHomeWorkDTOs(studentYearHomeWorks, homeWork.getId()))
                    .build();
            homeWorkDTOs.add(homeWorkDTO);
        }

        return homeWorkDTOs;
    }

    private List<StudentYearHomeWorkDto> getStudentYearHomeWorkDTOs(List<StudentYearHomeWork> studentYearHomeWorks, Long homeWorkId) {
        List<StudentYearHomeWorkDto> studentYearHomeWorkDTOs = new ArrayList<>();
        for (StudentYearHomeWork studentYearHomeWork : studentYearHomeWorks) {
            if (studentYearHomeWork.getHomeWork().getId().equals(homeWorkId)) {
                StudentYearHomeWorkDto studentYearHomeWorkDTO = StudentYearHomeWorkDto.builder()
                        .id(studentYearHomeWork.getId())
                        .description(studentYearHomeWork.getDescription())
                        .url(studentYearHomeWork.getUrl())
                        .submitTime(studentYearHomeWork.getSubmitTime())
                        .status(studentYearHomeWork.isStatus())
                        .statusName(studentYearHomeWork.getStatusName())
                        .point(studentYearHomeWork.getPoint())
                        .build();
                studentYearHomeWorkDTOs.add(studentYearHomeWorkDTO);
            }
        }
        return studentYearHomeWorkDTOs;
    }
















//    private final HomeWorkRepository homeWorkRepository;
//    private final StorageService storageService;
//    private final TeacherSchoolYearClassSubjectRepository teacherSchoolYearClassSubjectRepository;
//    private final StudentYearInfoRepository studentYearInfoRepository;
//    private final StudentYearHomeWorkRepository studentYearHomeWorkRepository;
//
//    public HomeWorkService(HomeWorkRepository homeWorkRepository, StorageService storageService, TeacherSchoolYearClassSubjectRepository teacherSchoolYearClassSubjectRepository, StudentYearInfoRepository studentYearInfoRepository, StudentYearHomeWorkRepository studentYearHomeWorkRepository) {
//        this.homeWorkRepository = homeWorkRepository;
//        this.storageService = storageService;
//        this.teacherSchoolYearClassSubjectRepository = teacherSchoolYearClassSubjectRepository;
//        this.studentYearInfoRepository = studentYearInfoRepository;
//        this.studentYearHomeWorkRepository = studentYearHomeWorkRepository;
//    }

//    public HomeWork createHomeWork(HomeWorkDto homeWorkDto) {
//        TeacherSchoolYearClassSubject teacherSchoolYearClassSubject = teacherSchoolYearClassSubjectRepository.findById(homeWorkDto.getTeacherSchoolYearClassSubjectId())
//                .orElseThrow(() -> new ResourceNotFoundException("TeacherSchoolYearClassSubject not found with id: " + homeWorkDto.getTeacherSchoolYearClassSubjectId()));
//
//        HomeWork homeWork = new HomeWork();
//        homeWork.setTitle(homeWorkDto.getTitle());
//        homeWork.setContent(homeWorkDto.getContent());
//        homeWork.setDueDate(homeWorkDto.getDueDate());
//        homeWork.setTeacherSchoolYearClassSubject(teacherSchoolYearClassSubject);
//        homeWork.setStatus(true);
//
//        // Save image files
//        List<String> imageUrls = new ArrayList<>();
//        for (MultipartFile file : homeWorkDto.getImageFiles()) {
//            String imageUrl = storageService.storeFile(file);
//            imageUrls.add(imageUrl);
//        }
//        homeWork.setImageUrls(imageUrls);
//
//        return homeWorkRepository.save(homeWork);
//    }
//
//    public StudentYearHomeWork createStudentYearHomeWork(StudentYearHomeWorkDto studentYearHomeWorkDto) {
//        HomeWork homeWork = homeWorkRepository.findById(studentYearHomeWorkDto.getHomeWorkId())
//                .orElseThrow(() -> new ResourceNotFoundException("HomeWork not found with id: " + studentYearHomeWorkDto.getHomeWorkId()));
//
//        StudentYearInfo studentYearInfo = studentYearInfoRepository.findById(studentYearHomeWorkDto.getStudentYearInfoId())
//                .orElseThrow(() -> new ResourceNotFoundException("StudentYearInfo not found with id: " + studentYearHomeWorkDto.getStudentYearInfoId()));
//
//        StudentYearHomeWork studentYearHomeWork = new StudentYearHomeWork();
//        studentYearHomeWork.setHomeWork(homeWork);
//        studentYearHomeWork.setStudentYearInfo(studentYearInfo);
//        studentYearHomeWork.setDescription(studentYearHomeWorkDto.getDescription());
//        studentYearHomeWork.setSubmitTime(new Date());
//
//        // Save image files
//        List<String> imgUrls = new ArrayList<>();
//        for (MultipartFile file : studentYearHomeWorkDto.getImageFiles()) {
//            String imgUrl = storageService.storeFile(file);
//            imgUrls.add(imgUrl);
//        }
//        studentYearHomeWork.setImageUrls(imgUrls);
//
//        return studentYearHomeWorkRepository.save(studentYearHomeWork);
//    }
//
//    public List<HomeWorkDto> getHomeWorksByStudentYearInfoId(Long studentYearInfoId) {
//        List<HomeWork> homeWorks = homeWorkRepository.findAll();
//        List<StudentYearHomeWork> studentYearHomeWorks = studentYearHomeWorkRepository.findByStudentYearInfo_Id(studentYearInfoId);
//
//        Map<Long, StudentYearHomeWork> studentYearHomeWorkMap = studentYearHomeWorks.stream()
//                .collect(Collectors.toMap(StudentYearHomeWork::getHomeWork_id, Function.identity()));
//
//        return homeWorks.stream()
//                .map(homeWork -> {
//                    HomeWorkDto homeWorkDto = new HomeWorkDto();
//                    homeWorkDto.setId(homeWork.getId());
//                    homeWorkDto.setTitle(homeWork.getTitle());
//                    homeWorkDto.setContent(homeWork.getContent());
//                    homeWorkDto.setDueDate(homeWork.getDueDate());
//                    homeWorkDto.setImageUrls(homeWork.getImageUrls());
//
//                    StudentYearHomeWork studentYearHomeWork = studentYearHomeWorkMap.get(homeWork.getId());
//                    if (studentYearHomeWork != null) {
//                        homeWorkDto.setStatus(studentYearHomeWork.isStatus());
//                        homeWorkDto.setStatusName(studentYearHomeWork.getStatusName());
//                        homeWorkDto.setPoint(studentYearHomeWork.getPoint());
//                        homeWorkDto.setSubmitTime(studentYearHomeWork.getSubmitTime());
//                        homeWorkDto.setDescription(studentYearHomeWork.getDescription());
//                    } else {
//                        homeWorkDto.setStatus(false);
//                        homeWorkDto.setStatusName("Chưa nộp");
//                        homeWorkDto.setPoint(0.0);
//                        homeWorkDto.setSubmitTime(null);
//                        homeWorkDto.setDescription(null);
//                    }
//
//                    // Kiểm tra thời hạn nộp bài
//                    if (homeWork.getDueDate().before(new Date())) {
//                        homeWorkDto.setStatus(false);
//                        homeWorkDto.setStatusName("Quá hạn");
//                    }
//
//                    return homeWorkDto;
//                })
//                .collect(Collectors.toList());
//    }
//
//    public List<HomeWorkDto> getHomeWorksByTeacherSchoolYearClassSubjectId(Long teacherSchoolYearClassSubjectId) {
//        List<HomeWork> homeWorks = homeWorkRepository.findByTeacherSchoolYearClassSubject_Id(teacherSchoolYearClassSubjectId);
//        List<StudentYearHomeWork> studentYearHomeWorks = studentYearHomeWorkRepository.findAll();
//
//        Map<Long, List<StudentYearHomeWork>> studentYearHomeWorkMap = studentYearHomeWorks.stream()
//                .collect(Collectors.groupingBy(StudentYearHomeWork::getHomeWork_id));
//
//        return homeWorks.stream()
//                .map(homeWork -> {
//                    HomeWorkDto homeWorkDto = new HomeWorkDto();
//                    homeWorkDto.setId(homeWork.getId());
//                    homeWorkDto.setTitle(homeWork.getTitle());
//                    homeWorkDto.setContent(homeWork.getContent());
//                    homeWorkDto.setDueDate(homeWork.getDueDate());
//                    homeWorkDto.setImageUrls(homeWork.getImageUrls());
//
//                    List<StudentYearHomeWork> relatedStudentYearHomeWorks = studentYearHomeWorkMap.get(homeWork.getId());
//                    if (relatedStudentYearHomeWorks != null) {
//                        long submittedCount = relatedStudentYearHomeWorks.stream()
//                                .filter(StudentYearHomeWork::isStatus)
//                                .count();
//                        homeWorkDto.setSubmittedCount(Math.toIntExact(submittedCount));
//                        homeWorkDto.setTotalStudentCount(relatedStudentYearHomeWorks.size());
//                    } else {
//                        homeWorkDto.setSubmittedCount(0);
//                        homeWorkDto.setTotalStudentCount(0);
//                    }
//
//                    return homeWorkDto;
//                })
//                .collect(Collectors.toList());
//    }
//
//    public HomeWorkDetailDto getHomeWorkDetails(Long homeWorkId) {
//        HomeWork homeWork = homeWorkRepository.findById(homeWorkId)
//                .orElseThrow(() -> new ResourceNotFoundException("HomeWork not found with id: " + homeWorkId));
//
//        List<StudentYearHomeWork> studentYearHomeWorks = studentYearHomeWorkRepository.findByHomeWorkId(homeWorkId);
//
//        HomeWorkDetailDto homeWorkDetailDto = new HomeWorkDetailDto();
//        homeWorkDetailDto.setId(homeWork.getId());
//        homeWorkDetailDto.setTitle(homeWork.getTitle());
//        homeWorkDetailDto.setContent(homeWork.getContent());
//        homeWorkDetailDto.setDueDate(homeWork.getDueDate());
//        homeWorkDetailDto.setImageUrls(homeWork.getImageUrls());
//
//        List<StudentYearHomeWorkDto> studentYearHomeWorkDtos = studentYearHomeWorks.stream()
//                .map(studentYearHomeWork -> {
//                    StudentYearHomeWorkDto studentYearHomeWorkDto = new StudentYearHomeWorkDto();
//                    studentYearHomeWorkDto.setId(studentYearHomeWork.getId());
//                    studentYearHomeWorkDto.setDescription(studentYearHomeWork.getDescription());
//                    studentYearHomeWorkDto.setImageUrls(studentYearHomeWork.getImageUrls());
//                    studentYearHomeWorkDto.setSubmitTime(studentYearHomeWork.getSubmitTime());
//                    studentYearHomeWorkDto.setStatus(studentYearHomeWork.isStatus());
//                    studentYearHomeWorkDto.setStatusName(studentYearHomeWork.getStatusName());
//                    studentYearHomeWorkDto.setPoint(studentYearHomeWork.getPoint());
//                    return studentYearHomeWorkDto;
//                })
//                .collect(Collectors.toList());
//
//        homeWorkDetailDto.setStudentYearHomeWorks(studentYearHomeWorkDtos);
//
//        return homeWorkDetailDto;
//    }

}
