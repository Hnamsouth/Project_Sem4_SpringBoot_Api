package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.HomeWorkDetailDto;
import com.example.project_sem4_springboot_api.dto.StudentYearHomeWorkDto;
import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.request.HomeWorkDto;
import com.example.project_sem4_springboot_api.exception.ResourceNotFoundException;
import com.example.project_sem4_springboot_api.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class HomeWorkService {

    private final HomeWorkRepository homeWorkRepository;
    private final StorageService storageService;
    private final TeacherSchoolYearClassSubjectRepository teacherSchoolYearClassSubjectRepository;
    private final StudentYearInfoRepository studentYearInfoRepository;
    private final StudentYearHomeWorkRepository studentYearHomeWorkRepository;

    public HomeWorkService(HomeWorkRepository homeWorkRepository, StorageService storageService, TeacherSchoolYearClassSubjectRepository teacherSchoolYearClassSubjectRepository, StudentYearInfoRepository studentYearInfoRepository, StudentYearHomeWorkRepository studentYearHomeWorkRepository) {
        this.homeWorkRepository = homeWorkRepository;
        this.storageService = storageService;
        this.teacherSchoolYearClassSubjectRepository = teacherSchoolYearClassSubjectRepository;
        this.studentYearInfoRepository = studentYearInfoRepository;
        this.studentYearHomeWorkRepository = studentYearHomeWorkRepository;
    }

    public HomeWork createHomeWork(HomeWorkDto homeWorkDto) {
        TeacherSchoolYearClassSubject teacherSchoolYearClassSubject = teacherSchoolYearClassSubjectRepository.findById(homeWorkDto.getTeacherSchoolYearClassSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("TeacherSchoolYearClassSubject not found with id: " + homeWorkDto.getTeacherSchoolYearClassSubjectId()));

        HomeWork homeWork = new HomeWork();
        homeWork.setTitle(homeWorkDto.getTitle());
        homeWork.setContent(homeWorkDto.getContent());
        homeWork.setDueDate(homeWorkDto.getDueDate());
        homeWork.setTeacherSchoolYearClassSubject(teacherSchoolYearClassSubject);
        homeWork.setStatus(true);

        // Save image files
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : homeWorkDto.getImageFiles()) {
            String imageUrl = storageService.storeFile(file);
            imageUrls.add(imageUrl);
        }
        homeWork.setImageUrls(imageUrls);

        return homeWorkRepository.save(homeWork);
    }

    public StudentYearHomeWork createStudentYearHomeWork(StudentYearHomeWorkDto studentYearHomeWorkDto) {
        HomeWork homeWork = homeWorkRepository.findById(studentYearHomeWorkDto.getHomeWorkId())
                .orElseThrow(() -> new ResourceNotFoundException("HomeWork not found with id: " + studentYearHomeWorkDto.getHomeWorkId()));

        StudentYearInfo studentYearInfo = studentYearInfoRepository.findById(studentYearHomeWorkDto.getStudentYearInfoId())
                .orElseThrow(() -> new ResourceNotFoundException("StudentYearInfo not found with id: " + studentYearHomeWorkDto.getStudentYearInfoId()));

        StudentYearHomeWork studentYearHomeWork = new StudentYearHomeWork();
        studentYearHomeWork.setHomeWork(homeWork);
        studentYearHomeWork.setStudentYearInfo(studentYearInfo);
        studentYearHomeWork.setDescription(studentYearHomeWorkDto.getDescription());
        studentYearHomeWork.setSubmitTime(new Date());

        // Save image files
        List<String> imgUrls = new ArrayList<>();
        for (MultipartFile file : studentYearHomeWorkDto.getImageFiles()) {
            String imgUrl = storageService.storeFile(file);
            imgUrls.add(imgUrl);
        }
        studentYearHomeWork.setImageUrls(imgUrls);

        return studentYearHomeWorkRepository.save(studentYearHomeWork);
    }

    public List<HomeWorkDto> getHomeWorksByStudentYearInfoId(Long studentYearInfoId) {
        List<HomeWork> homeWorks = homeWorkRepository.findAll();
        List<StudentYearHomeWork> studentYearHomeWorks = studentYearHomeWorkRepository.findByStudentYearInfo_Id(studentYearInfoId);

        Map<Long, StudentYearHomeWork> studentYearHomeWorkMap = studentYearHomeWorks.stream()
                .collect(Collectors.toMap(StudentYearHomeWork::getHomeWork_id, Function.identity()));

        return homeWorks.stream()
                .map(homeWork -> {
                    HomeWorkDto homeWorkDto = new HomeWorkDto();
                    homeWorkDto.setId(homeWork.getId());
                    homeWorkDto.setTitle(homeWork.getTitle());
                    homeWorkDto.setContent(homeWork.getContent());
                    homeWorkDto.setDueDate(homeWork.getDueDate());
                    homeWorkDto.setImageUrls(homeWork.getImageUrls());

                    StudentYearHomeWork studentYearHomeWork = studentYearHomeWorkMap.get(homeWork.getId());
                    if (studentYearHomeWork != null) {
                        homeWorkDto.setStatus(studentYearHomeWork.isStatus());
                        homeWorkDto.setStatusName(studentYearHomeWork.getStatusName());
                        homeWorkDto.setPoint(studentYearHomeWork.getPoint());
                        homeWorkDto.setSubmitTime(studentYearHomeWork.getSubmitTime());
                        homeWorkDto.setDescription(studentYearHomeWork.getDescription());
                    } else {
                        homeWorkDto.setStatus(false);
                        homeWorkDto.setStatusName("Chưa nộp");
                        homeWorkDto.setPoint(0.0);
                        homeWorkDto.setSubmitTime(null);
                        homeWorkDto.setDescription(null);
                    }

                    // Kiểm tra thời hạn nộp bài
                    if (homeWork.getDueDate().before(new Date())) {
                        homeWorkDto.setStatus(false);
                        homeWorkDto.setStatusName("Quá hạn");
                    }

                    return homeWorkDto;
                })
                .collect(Collectors.toList());
    }

    public List<HomeWorkDto> getHomeWorksByTeacherSchoolYearClassSubjectId(Long teacherSchoolYearClassSubjectId) {
        List<HomeWork> homeWorks = homeWorkRepository.findByTeacherSchoolYearClassSubject_Id(teacherSchoolYearClassSubjectId);
        List<StudentYearHomeWork> studentYearHomeWorks = studentYearHomeWorkRepository.findAll();

        Map<Long, List<StudentYearHomeWork>> studentYearHomeWorkMap = studentYearHomeWorks.stream()
                .collect(Collectors.groupingBy(StudentYearHomeWork::getHomeWork_id));

        return homeWorks.stream()
                .map(homeWork -> {
                    HomeWorkDto homeWorkDto = new HomeWorkDto();
                    homeWorkDto.setId(homeWork.getId());
                    homeWorkDto.setTitle(homeWork.getTitle());
                    homeWorkDto.setContent(homeWork.getContent());
                    homeWorkDto.setDueDate(homeWork.getDueDate());
                    homeWorkDto.setImageUrls(homeWork.getImageUrls());

                    List<StudentYearHomeWork> relatedStudentYearHomeWorks = studentYearHomeWorkMap.get(homeWork.getId());
                    if (relatedStudentYearHomeWorks != null) {
                        long submittedCount = relatedStudentYearHomeWorks.stream()
                                .filter(StudentYearHomeWork::isStatus)
                                .count();
                        homeWorkDto.setSubmittedCount(Math.toIntExact(submittedCount));
                        homeWorkDto.setTotalStudentCount(relatedStudentYearHomeWorks.size());
                    } else {
                        homeWorkDto.setSubmittedCount(0);
                        homeWorkDto.setTotalStudentCount(0);
                    }

                    return homeWorkDto;
                })
                .collect(Collectors.toList());
    }

    public HomeWorkDetailDto getHomeWorkDetails(Long homeWorkId) {
        HomeWork homeWork = homeWorkRepository.findById(homeWorkId)
                .orElseThrow(() -> new ResourceNotFoundException("HomeWork not found with id: " + homeWorkId));

        List<StudentYearHomeWork> studentYearHomeWorks = studentYearHomeWorkRepository.findByHomeWorkId(homeWorkId);

        HomeWorkDetailDto homeWorkDetailDto = new HomeWorkDetailDto();
        homeWorkDetailDto.setId(homeWork.getId());
        homeWorkDetailDto.setTitle(homeWork.getTitle());
        homeWorkDetailDto.setContent(homeWork.getContent());
        homeWorkDetailDto.setDueDate(homeWork.getDueDate());
        homeWorkDetailDto.setImageUrls(homeWork.getImageUrls());

        List<StudentYearHomeWorkDto> studentYearHomeWorkDtos = studentYearHomeWorks.stream()
                .map(studentYearHomeWork -> {
                    StudentYearHomeWorkDto studentYearHomeWorkDto = new StudentYearHomeWorkDto();
                    studentYearHomeWorkDto.setId(studentYearHomeWork.getId());
                    studentYearHomeWorkDto.setDescription(studentYearHomeWork.getDescription());
                    studentYearHomeWorkDto.setImageUrls(studentYearHomeWork.getImageUrls());
                    studentYearHomeWorkDto.setSubmitTime(studentYearHomeWork.getSubmitTime());
                    studentYearHomeWorkDto.setStatus(studentYearHomeWork.isStatus());
                    studentYearHomeWorkDto.setStatusName(studentYearHomeWork.getStatusName());
                    studentYearHomeWorkDto.setPoint(studentYearHomeWork.getPoint());
                    return studentYearHomeWorkDto;
                })
                .collect(Collectors.toList());

        homeWorkDetailDto.setStudentYearHomeWorks(studentYearHomeWorkDtos);

        return homeWorkDetailDto;
    }

}
