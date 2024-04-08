package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.EGrade;
import com.example.project_sem4_springboot_api.entities.enums.ESem;
import com.example.project_sem4_springboot_api.entities.request.*;
import com.example.project_sem4_springboot_api.repositories.*;
import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl {
    static final int TONG_TIET_HOC_1_TUAN = 35;
    private final SchoolYearRepository schoolYearRepository;
    private final SchoolYearSubjectRepository schoolYearSubjectRepository;
    private final SchoolYearClassRepository schoolYearClassRepository;
    private final SchoolYearSubjectGradeRepository schoolYearSubjectGradeRepository;
    private final TeacherSchoolYearRepository teacherSchoolYearRepository;
    private final TeacherRepository teacherRepository;
    private final GradeRepository gradeRepository;
    private final SubjectRepository subjectRepository;
    private final RoomRepository roomRepository;
    private final ScheduleRepository scheduleRepository;
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
                if(schoolYearSubjectRepository.existsBySubject_Id(data.getSubject())){
                    return ResponseEntity.badRequest().body("Subject Exist !!!");
                }
                var createdData = schoolYearSubjectRepository.save(
                        SchoolYearSubject.builder()
                                .subject(subjectRepository.findById(data.getSubject()).orElseThrow(
                                        ()->new NullPointerException("Subject Invalid !!!")
                                ))
                                .schoolYear(schoolYearRepository.findById(data.getSchoolYear()).orElseThrow(
                                        ()->new NullPointerException("Invalid School Year !!!")
                                ))
                                .build()
                );
                return ResponseEntity.ok(Map.entry("Create School Year Subject Success",createdData));
            }
            if(!data.getSubjects().isEmpty()){
                if(schoolYearSubjectRepository.existsBySubject_IdIn(data.getSubjects())){
                    return ResponseEntity.badRequest().body("Some Subject Exist !!!");
                }
                var subjects = subjectRepository.findAllById(data.getSubjects());
                if(!subjects.isEmpty()){
                    var schoolYear = schoolYearRepository.findById(data.getSchoolYear()).orElseThrow(
                            ()->new NullPointerException("Invalid School Year !!!")
                    );
                    var createdData =  schoolYearSubjectRepository.saveAll(
                            subjects.stream().map((s)-> SchoolYearSubject.builder()
                                    .subject(s)
                                    .schoolYear(schoolYear)
                                    .build()).toList()
                    );
                    return ResponseEntity.ok(Map.entry("Create School Year Subject Success",createdData));
                }
                return ResponseEntity.badRequest().body("Subject List Invalid !!!");
            }
            return ResponseEntity.badRequest().body("Subjects Empty !!!");

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public ResponseEntity<?> createTeacherSchoolYear(TeacherSchoolYearDto data){
        try {
            if(data.getTeacher() != null ){
                if(teacherSchoolYearRepository.existsByTeacher_Id(data.getTeacher())){
                    return ResponseEntity.badRequest().body("Teachers have been added for the current school year!!!");
                }
                var createdData = teacherSchoolYearRepository.save(
                    TeacherSchoolYear.builder()
                        .teacher(teacherRepository.findById(data.getTeacher()).orElseThrow(
                            ()->new NullPointerException("Teacher Not Found !!!")
                        ))
                        .schoolYear(schoolYearRepository.findById(data.getSchoolYear()).orElseThrow(
                                ()->new NullPointerException("School Year Not Found !!!")
                        ))
                        .build()
                );
                return ResponseEntity.ok(Map.entry("Create Teacher School Year Success",createdData));
            }
            if(!data.getTeachers().isEmpty()){
                if(teacherSchoolYearRepository.existsByTeacher_IdIn(data.getTeachers())){
                    return ResponseEntity.badRequest().body("Some Teachers have been added for the current school year!!!");
                }
                var teachers = teacherRepository.findAllById(data.getTeachers());
                if(!teachers.isEmpty()){
                    var schoolYear = schoolYearRepository.findById(data.getSchoolYear()).orElseThrow(
                            ()->new NullPointerException("School Year Not Found !!!")
                    );
                    var createdData =  teacherSchoolYearRepository.saveAll(
                        teachers.stream().map((t)-> TeacherSchoolYear.builder()
                            .teacher(t)
                            .schoolYear(schoolYear)
                            .build()).toList()
                    );
                    return ResponseEntity.ok(Map.entry("Create List Teacher School Year Success",createdData));
                }
                return ResponseEntity.badRequest().body("Teachers List Invalid !!!");
            }
            return ResponseEntity.badRequest().body("Teachers Empty !!!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public ResponseEntity<?> createSchoolYearClass(SchoolYearClassDto data){
        try{

            var schoolYear = schoolYearRepository.findById(data.getSchoolYear()).orElseThrow(
                    ()->new NullPointerException("Not Found school year !!!")
            );
            if(schoolYearClassRepository.existsByClassNameAndSchoolYear_Id(data.getClassName(),data.getSchoolYear())){
                return ResponseEntity.badRequest().body("Class name Exist !!!");
            }
            if(schoolYearClassRepository.existsByTeacherSchoolYear_IdAndSchoolYear_Id(data.getTeacherSchoolYear(),data.getSchoolYear())){
                return ResponseEntity.badRequest().body("Teacher have been added for a Class !!!");
            }
            var createdData=  schoolYearClassRepository.save(
                SchoolYearClass.builder()
                    .className(data.getClassName())
                    .classCode(data.getClassCode())
                    .teacherSchoolYear(teacherSchoolYearRepository.findById(data.getTeacherSchoolYear()).orElseThrow(
                        ()->new NullPointerException("Not Found teacher !!!")
                    ))
                    .grade(gradeRepository.findById(data.getGrade()).orElseThrow(
                        ()->new NullPointerException("Not Found grade !!!")
                    ))
                    .room(roomRepository.findById(data.getRoom()).orElseThrow(
                        ()->new NullPointerException("Not Found room !!!")
                    ))
                    .schoolYear(schoolYear)
                    .build()
            );
            return ResponseEntity.ok(Map.entry("Create School Year Class Success",createdData));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public ResponseEntity<?> createSchoolYearSubjectGrade(SchoolYearSubjectGradeCreate data){
        try {
            var schoolYearSubject = schoolYearSubjectRepository.findById(data.getSchoolYearSubjectId()).orElseThrow(
                    ()->new NullPointerException("Not Found School Year Subject !!!")
            );
            if(checkPeriod(schoolYearSubject.getSchoolYear(),data.getNumber())){
                return ResponseEntity.badRequest().body("Period of year is full !!!");
            }
            var result =  schoolYearSubjectGradeRepository.save(
                SchoolYearSubjectGrade.builder()
                    .schoolYearSubject(schoolYearSubjectRepository.findById(data.getSchoolYearSubjectId()).orElseThrow(
                        ()->new NullPointerException("Not Found School Year Subject !!!")
                    ))
                    .grade(gradeRepository.findById(data.getGradeId()).orElseThrow(
                        ()->new NullPointerException("Not Found Grade !!!")
                    ))
                    .sem(data.getSem())
                    .number(data.getNumber())
                .build());
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
    * 1: read subject
    * 2: read school year
    * 3: read school year class
    * 4: read schedule
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
    public ResponseEntity<?> getSchoolYear(@Nullable Long id){
        try {
            if(id!=null){
                return ResponseEntity.ok(schoolYearRepository
                    .findById(id)
                    .orElseThrow(()->new NullPointerException("Not Found!!!")));
            }
            return ResponseEntity.ok(schoolYearRepository.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public ResponseEntity<?> getSchoolYearSubject(@Nullable Long id, @Nullable Long schoolYearId, @Nullable List<Long> subjectIds){
        try {
            if(id!=null){
                return ResponseEntity.ok(schoolYearSubjectRepository
                    .findById(id)
                    .orElseThrow(()->new NullPointerException("Not Found!!!")));
            }
            if(schoolYearId!=null && subjectIds !=null){
                return ResponseEntity.ok(schoolYearSubjectRepository
                    .findAllBySchoolYear_IdAndSubject_IdIn(schoolYearId,subjectIds)
                );
            }
            if(schoolYearId!=null || subjectIds !=null){
                return ResponseEntity.ok(schoolYearSubjectRepository
                        .findAllBySchoolYear_IdOrSubject_IdIn(schoolYearId,subjectIds)
                );
            }
            return ResponseEntity.badRequest().body("Require less schoolYear param for search !!!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public ResponseEntity<?> getTeacherSchoolYear(@Nullable Long id,@Nullable Long teacherId,@Nullable Long schoolYearId){
        try {
            if(id!=null){
                return ResponseEntity.ok(teacherSchoolYearRepository
                    .findById(id)
                    .orElseThrow(()->new NullPointerException("Not Found!!!")));
            }
            if(teacherId!=null && schoolYearId !=null){
                return ResponseEntity.ok(teacherSchoolYearRepository
                    .findAllByTeacher_IdAndSchoolYear_Id(teacherId,schoolYearId)
                );
            }
            if(teacherId!=null || schoolYearId !=null){
                return ResponseEntity.ok(teacherSchoolYearRepository
                        .findAllByTeacher_IdOrSchoolYear_Id(teacherId,schoolYearId)
                );
            }
            return ResponseEntity.badRequest().body("Require less schoolYear param for search !!!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    /*
     * get list class by school year
     * get list class by school year & grade
    * get list class by school year & teacher
    * */
    public ResponseEntity<?> getSchoolYearClass(
            @Nullable Long id,
            @Nullable Long gradeId,
            @Nullable Long schoolYearId,
            @Nullable Long teacherSchoolYearId,
            @Nullable Long roomId,
            @Nullable String className,
            @Nullable String classCode
    ){
        try {
            if(schoolYearId!=null){
                if(gradeId!=null){
                    return ResponseEntity.ok(schoolYearClassRepository
                        .findAllBySchoolYear_IdAndGrade_Id(schoolYearId,gradeId));
                }
                if(teacherSchoolYearId!=null) {
                    return ResponseEntity.ok(schoolYearClassRepository
                            .findAllByTeacherSchoolYear_IdAndSchoolYear_Id(schoolYearId, teacherSchoolYearId));
                }
                return ResponseEntity.ok(schoolYearClassRepository
                        .findAllBySchoolYear_Id(schoolYearId));
            }
            var result = schoolYearClassRepository
                    .findAllByIdOrClassNameOrClassCodeOrTeacherSchoolYear_IdOrSchoolYear_IdOrGrade_IdOrRoom_Id(
                            id,className,classCode,teacherSchoolYearId,schoolYearId,gradeId,roomId
                    );
            return result.size() > 0 ? ResponseEntity.ok(result) :
                    ResponseEntity.notFound().eTag("Require param for search !!!").build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /*
    * get subject by id
    * get list subject by grade
    * get list subject by grade between number
    * get list subject by grade and sem
    * */
    public ResponseEntity<?> getSchoolYearSubjectGrade(
            @Nullable Long id,
            @Nullable Long schoolYearSubjectId,
            @Nullable Long gradeId,
            @Nullable Integer number,
            @Nullable ESem sem
    ){
        try {
            if(gradeId!=null){
                if(sem!=null){
                    return ResponseEntity.ok(schoolYearSubjectGradeRepository
                            .findAllByGrade_IdAndSemIsLike(gradeId,sem));
                }
                if(number!=null){
                    return ResponseEntity.ok(schoolYearSubjectGradeRepository
                        .findAllByGrade_IdAndNumber(gradeId,number));
                }
                return ResponseEntity.ok(schoolYearSubjectGradeRepository
                        .findAllByGrade_Id(gradeId));
            }
            var result = schoolYearSubjectGradeRepository
                    .findAllByIdOrSchoolYearSubject_Id(id,schoolYearSubjectId);
            return result.size() > 0 ? ResponseEntity.ok(result) :
             ResponseEntity.badRequest().body("Require less schoolYear param for search !!!");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public ResponseEntity<?> getSchedule(@Nullable Long classId){
        try {
            var schoolYearClass = schoolYearClassRepository.findById(classId).orElseThrow(()->new NullPointerException("Class Not Found!!!"));
            var schedule =scheduleRepository.findAllBySchoolYearClass(schoolYearClass);
            if(schedule.isEmpty()){
                return ResponseEntity.badRequest().body("Not Found !!!");
            }
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private boolean checkPeriod(SchoolYear schoolYear,int addPeriod){
        var month = ChronoUnit.MONTHS.between(
                LocalDate.parse(schoolYear.getStartSem1().toString().substring(0,10)),
                LocalDate.parse(schoolYear.getEnd().toString().substring(0,10))
        );
        var numberPeriodOfYear = month*TONG_TIET_HOC_1_TUAN;
        var currentPeriod =  schoolYearSubjectGradeRepository.getPeriodOfSchoolYearAndGrade(1L,1L);
//        schoolYearSubjectGradeRepository
        return currentPeriod > numberPeriodOfYear;
    }
}
