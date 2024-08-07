package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.dto.StudentYearHomeWorkDto;
import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.request.HomeWorkDto;
import com.example.project_sem4_springboot_api.exception.ArgumentNotValidException;
import com.example.project_sem4_springboot_api.repositories.*;
import com.example.project_sem4_springboot_api.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
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

    public HomeWork createHomeWork(String title,
                                   String content,
                                   String dueDate,
                                   String startDate,
                                   Long teacherSchoolYearClassSubjectId,
                                   List<MultipartFile> images) throws ParseException, IOException, ExecutionException, InterruptedException {
        TeacherSchoolYearClassSubject teacherSchoolYearClassSubject = teacherSchoolYearClassSubjectRepository.findById(teacherSchoolYearClassSubjectId)
                .orElseThrow(() -> new RuntimeException("TeacherSchoolYearClassSubject not found"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sd = sdf.parse(startDate);
        Date dd = sdf.parse(dueDate);
        if(dd.before(sd)) throw new ArgumentNotValidException("","", "Ngày kết thúc không thể trước ngày bắt đầu");

        HomeWork homeWork = new HomeWork();
        homeWork.setTitle(title);
        homeWork.setContent(content);
        homeWork.setDueDate(dd);
        homeWork.setStartDate(sd);
        homeWork.setTeacherSchoolYearClassSubject(teacherSchoolYearClassSubject);
        homeWork.setStatus(true);
        homeWork.setStatusName("Đang hoạt động");
        var saveHomeWork = homeWorkRepository.save(homeWork);
        if(!images.isEmpty()){
            cloudinaryService.uploadMultiImage(images,HOMEWORK_TAG + saveHomeWork.getId(), HOMEWORK_FN);
            saveHomeWork.setUrl(HOMEWORK_TAG + saveHomeWork.getId());
            homeWorkRepository.save(saveHomeWork);
        }
        return saveHomeWork;
    }

    public StudentYearHomeWork submitHomeWork(List<MultipartFile> images, String description, Long studentYearInfoId, Long homeWorkId) throws Exception {
        HomeWork homeWork = homeWorkRepository.findById(homeWorkId)
                .orElseThrow(() -> new RuntimeException("HomeWork khong ton tai"));

        if (homeWork.getDueDate().before(new Date())) throw new RuntimeException("bai tap dahet han");

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
            cloudinaryService.removeFileByTag(s.getUrl(),STUDENT_HOMEWORK_FN);
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
        if(!images.isEmpty()){
            cloudinaryService.uploadMultiImage(images,STUDENT_HOMEWORK_TAG + studentYearHomeWork.getId(), STUDENT_HOMEWORK_FN);
            saveStudentYearHomeWork.setUrl(STUDENT_HOMEWORK_TAG + studentYearHomeWork.getId());
            studentYearHomeWorkRepository.save(saveStudentYearHomeWork);
        }
        return saveStudentYearHomeWork;
    }

    public List<HomeWorkDto> getHomeWorksByStudentYearInfoId(Long studentYearInfoId) {
        var student = studentYearInfoRepository.findById(studentYearInfoId).orElseThrow(() ->
                new ArgumentNotValidException("Student not found", "", ""));

        List<HomeWork> homeWorks = homeWorkRepository.
                findAllByTeacherSchoolYearClassSubject_SchoolYearClass_Id(student.getSchoolYearClass().getId());
        List<StudentYearHomeWork> studentYearHomeWorks = studentYearHomeWorkRepository.findAllByStudentYearInfo_IdAndHomeWorkIn(studentYearInfoId, homeWorks);
        // get all tags
        List<String> tags = new ArrayList<>(homeWorks.stream().map(HomeWork::getUrl).toList());
        tags.addAll(studentYearHomeWorks.stream().map(StudentYearHomeWork::getUrl).toList());
        // get all images by tags
        var listImagesUrl = cloudinaryService.getImageGroupByTags(tags);

        return homeWorks.stream().map(s->{
            var hw = s.convertToDto();
            var st = studentYearHomeWorks.stream().filter(h->h.getHomeWork().getId().equals(s.getId())).findFirst();
            if(st.isPresent()){
                var stdHw = st.get().convertToDtoNoStdInfo();
                stdHw.setImageUrl(listImagesUrl.get(stdHw.getUrl()));
                hw.setStudentYearHomeWorks(List.of(stdHw));
            }
            hw.setHomeworkImageUrls(listImagesUrl.get(s.getUrl()));
            hw.setSubmission(st.isPresent());
            return hw;
        }).toList();
    }

    public HomeWorkDto getHomeWorkDetail(Long homeWorkId) {
        HomeWork homeWork = homeWorkRepository.findById(homeWorkId)
                .orElseThrow(() -> new RuntimeException("HomeWork không tồn tại"));

        List<StudentYearHomeWork> studentHomeWorks = homeWork.getStudentYearHomeWorks();

        List<String> listTags = new LinkedList<>();
        listTags.add(homeWork.getUrl());
        studentHomeWorks.forEach(s->listTags.add(s.getUrl()));

        var listImagesUrl = cloudinaryService.getImageGroupByTags(listTags);
        List<StudentYearHomeWorkDto> studentHomeWorkDtos = studentHomeWorks.stream()
                .map(s->{
                    var student = s.convertToDto();
                    student.setImageUrl(listImagesUrl.get(student.getUrl()));
                    return student;
                })
                .collect(Collectors.toList());

        var res = homeWork.convertToDtoOnlyHw();
        res.setStudentYearHomeWorks(studentHomeWorkDtos);
        res.setHomeworkImageUrls(listImagesUrl.get(homeWork.getUrl()));

        return res;
    }

    /*
         4: lấy ds bài tập đã giao của giáo viên theo id phân công giảng dạy (techer_schoolyear_class_subject)
        DB(HomeWork)  , method: GET , params: techer_schoolyear_class_subject_id

         return : trả về danh sách bài tập + (số lượng hs đã đã nộp / sl hs của lớp )
         trả về ds url ảnh bài tập và ảnh bài nộp nếu có
    */
    public ResponseEntity<?> getHomeWorksByTeacher(Long teacherSchoolYearClassSubjectId){
        List<HomeWork> homeWorks = homeWorkRepository.findAllByTeacherSchoolYearClassSubjectId(teacherSchoolYearClassSubjectId);
        var listImg =  cloudinaryService.getImageGroupByTags(homeWorks.stream().map(HomeWork::getUrl).toList());

        var res = homeWorks.stream().map(h->{
            var hw = h.toTeacherRes();
            hw.put("homeworkImageUrls",listImg.get(h.getUrl()));
            return hw;
        }).toList();
        return ResponseEntity.ok(res);
    }


}















