package com.example.project_sem4_springboot_api.service.impl;
import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.ESem;
import com.example.project_sem4_springboot_api.entities.request.*;
import com.example.project_sem4_springboot_api.repositories.*;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.naming.ContextNotEmptyException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.EmptyStackException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolServiceImpl {
    static final int TONG_TIET_HOC_1_TUAN = 35;
    private final SchoolYearRepository schoolYearRepository;
    private final SchoolYearSubjectRepository schoolYearSubjectRepository;
    private final SchoolYearClassRepository schoolYearClassRepository;
    private final SchoolYearSubjectGradeRepository schoolYearSubjectGradeRepository;
    private final TeacherSchoolYearRepository teacherSchoolYearRepository;
    private final TeacherSchoolYearClassSubjectRepository teacherSchoolYearClassSubjectRepository;
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
    * 5: create schoolyear-subject-grade
    * 6: create teacher schoolyear subject
    * 7: create schedule
    * */

    public ResponseEntity<?> createSchoolYear(SchoolYearCreate data){
        try {
            Calendar sem1 = Calendar.getInstance();
            sem1.setTime(data.getStartSem1());
            var check  = schoolYearRepository.getAllYear();
            if(check.contains(sem1.get(Calendar.YEAR))){
                return ResponseEntity.badRequest().body("School Year Exist !!!");
            }
            var createdData =  schoolYearRepository.save(
                SchoolYear.builder()
                    .startSem1(data.getStartSem1())
                    .startSem2(data.getStartSem2())
                    .end(data.getEnd())
                    .build()
            );
            return ResponseEntity.ok(createdData);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public ResponseEntity<?> createSchoolYearSubject(SchoolYearSubjectCreate data){
        try {
            var schoolYear = schoolYearRepository.findById(data.getSchoolYear()).orElseThrow(
                    ()->new NullPointerException("Not Found School Year !!!")
            );
            if(data.getSubject() != null){
                if(schoolYearSubjectRepository.existsBySubject_Id(data.getSubject())){
                    return ResponseEntity.badRequest().body("Subject Exist !!!");
                }
                var createdData = schoolYearSubjectRepository.save(
                        SchoolYearSubject.builder()
                                .subject(subjectRepository.findById(data.getSubject()).orElseThrow(
                                        ()->new NullPointerException("Subject Invalid !!!")
                                ))
                                .schoolYear(schoolYear)
                                .build()
                );
                return ResponseEntity.ok(createdData);
            }
            if(!data.getSubjects().isEmpty()){
                var subjects = subjectRepository.findAllById(data.getSubjects());
                if(!subjects.isEmpty()){
                    if(schoolYearSubjectRepository.existsBySubject_IdIn(data.getSubjects())){
                        return ResponseEntity.badRequest().body("Some Subject Exist !!!");
                    }
                    var createdData =  schoolYearSubjectRepository.saveAll(
                            subjects.stream().map((s)-> SchoolYearSubject.builder()
                                    .subject(s)
                                    .schoolYear(schoolYear)
                                    .build()).toList()
                    );
                    return ResponseEntity.ok(createdData);
                }
                return ResponseEntity.badRequest().body("Subject List Invalid !!!");
            }
            return ResponseEntity.badRequest().body("Subjects Empty !!!");

        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public ResponseEntity<?> createTeacherSchoolYear(TeacherSchoolYearCreate data){
        try {
            var schoolYear = schoolYearRepository.findById(data.getSchoolYear()).orElseThrow(
                 ()->new NullPointerException("School Year Not Found !!!")
            );
            if(data.getTeacher() != null ){
                if(teacherSchoolYearRepository.existsByTeacher_IdAndSchoolYear_Id(data.getTeacher(),data.getSchoolYear())){
                    return ResponseEntity.badRequest().body("Teachers have been added for the current school year!!!");
                }
                var createdData = teacherSchoolYearRepository.save(
                    TeacherSchoolYear.builder()
                        .teacher(teacherRepository.findById(data.getTeacher()).orElseThrow(
                            ()->new NullPointerException("Teacher Not Found !!!")
                        ))
                        .schoolYear(schoolYear)
                        .build()
                );
                return ResponseEntity.ok(createdData);
            }
            if(!data.getTeachers().isEmpty()){
                var teachers = teacherRepository.findAllById(data.getTeachers());
                if(!teachers.isEmpty()){
                    if(teacherSchoolYearRepository.existsByTeacher_IdInAndSchoolYear_Id(data.getTeachers(),data.getSchoolYear())){
                        return ResponseEntity.badRequest().body("Some Teachers have been added for the current school year!!!");
                    }
                    var createdData =  teacherSchoolYearRepository.saveAll(
                        teachers.stream().map((t)-> TeacherSchoolYear.builder()
                            .teacher(t)
                            .schoolYear(schoolYear)
                            .build()).toList()
                    );
                    return ResponseEntity.ok(createdData);
                }
                return ResponseEntity.badRequest().body("Teachers List Invalid !!!");
            }
            return ResponseEntity.badRequest().body("Teachers Empty !!!");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public ResponseEntity<?> createSchoolYearClass(SchoolYearClassCreate data){
        try{
            var schoolYear = schoolYearRepository.findById(data.getSchoolYear()).orElseThrow(
                    ()->new NullPointerException("Not Found school year !!!")
            );
            // check class name exist
            if(schoolYearClassRepository.existsByClassNameAndSchoolYear_Id(data.getClassName(),data.getSchoolYear())){
                return ResponseEntity.badRequest().body("Class name Exist !!!");
            }
            // check class code exist
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
                    .grade(gradeRepository.findById(data.getGradeId()).orElseThrow(
                        ()->new NullPointerException("Not Found grade !!!")
                    ))
                    .room(roomRepository.findById(data.getRoomId()).orElseThrow(
                        ()->new NullPointerException("Not Found room !!!")
                    ))
                    .schoolYear(schoolYear)
                    .build()
            );
            return ResponseEntity.ok(createdData);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    public ResponseEntity<?> createSchoolYearSubjectGrade(SchoolYearSubjectGradeCreate data){
        try {
            // check subject exist
            var schoolYearSubject = schoolYearSubjectRepository.findById(data.getSchoolYearSubjectId()).orElseThrow(
                    ()->new NullPointerException("Not Found School Year Subject !!!")
            );
            // check period of year
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
    public ResponseEntity<?> createTeacherSchoolYearClassSubject(TeacherSchoolYearClassSubjectCreate data) {
        List<TeacherSchoolYearClassSubject> result = new ArrayList<>();
        var teacherSchoolYear = teacherSchoolYearRepository.findByTeacherId(data.getTeacherSchoolYearId());
        if(teacherSchoolYear == null) {
            throw new NullPointerException("Không tìm thấy Giáo viên với id : "+data.getTeacherSchoolYearId()+"!!!") ;
        }
        data.getSubjectClassList().forEach(e->{
            // check subject exist
            var schoolYearSubject = schoolYearSubjectRepository.findById(e.getSchoolYearSubjectId()).orElseThrow(
                    ()->new NullPointerException("Không tìm thấy Môn học với Id: "+e.getSchoolYearSubjectId()+" !!!")
            );
            List<TeacherSchoolYearClassSubject> teacherSchoolYearClassSubjects = new ArrayList<>();
            e.getSchoolYearClassId().forEach(c->{
                // check subject exist
                var schoolYearClass =  schoolYearClassRepository.findById(c).orElseThrow(
                        ()->new NullPointerException("Không tìm thấy Lớp học với  Id: "+c+" !!!")
                );
                // check teacher teach subject to class yet
                if(teacherSchoolYearClassSubjectRepository.findByTeacherSchoolYear_IdAndSchoolYearClass_IdAndTeacherSchoolYear_Id(
                        teacherSchoolYear.getId(),c,e.getSchoolYearSubjectId()
                )){
                    throw new NullPointerException("Giáo viên: "+teacherSchoolYear.getId()+" đã dạy subjectId: "+e.getSchoolYearSubjectId()+" ở lớp "+c+" !!!");
                }
                // add teacher teach subject to class
                teacherSchoolYearClassSubjects.add(TeacherSchoolYearClassSubject.builder()
                        .teacherSchoolYear(teacherSchoolYear)
                        .schoolYearSubject(schoolYearSubject)
                        .schoolYearClass(schoolYearClass)
                        .build());
            });
            // save teacher teach subject to class
            result.addAll(teacherSchoolYearClassSubjectRepository.saveAll(teacherSchoolYearClassSubjects));
        });
        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }

    public ResponseEntity<?> createSchedule(ScheduleCreate data){
        try {
            return ResponseEntity.ok("Create Schedule Success");
        } catch (Exception e) {
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
        if(id!=null){
            return ResponseEntity.ok(subjectRepository.findById(id).orElseThrow(
                    ()->new NullPointerException("Không tìm thấy Subject vi id: "+id+" !!!")
            ));
        }
        return ResponseEntity.ok(subjectRepository.findAll());
    }
    public ResponseEntity<?> getSchoolYear(@Nullable Long id){
        if(id!=null){
            return ResponseEntity.ok(schoolYearRepository.findById(id)
                    .orElseThrow(()->new NullPointerException("Không tìm thấy schoolYear với id: "+id+"!!!")));
        }
        return ResponseEntity.ok(schoolYearRepository.findAll());
    }
    public ResponseEntity<?> getSchoolYearSubject(@Nullable Long id, @Nullable Long schoolYearId, @Nullable List<Long> subjectIds){
        if(id!=null){
            return ResponseEntity.ok(schoolYearSubjectRepository.findById(id)
                    .orElseThrow(()->new NullPointerException("Không tìm thấy Môn học với ID = "+id+" !!!")));
        }
        if(schoolYearId!=null && subjectIds !=null){
            return checkListEmptyGetResponse(
                    schoolYearSubjectRepository.findAllBySchoolYear_IdAndSubject_IdIn(schoolYearId,subjectIds),
                    "Không tìm thấy Môn học với schoolYearId: "+ schoolYearId +" & dánh sách subjectIds !!!"
            );
        }
        if(schoolYearId!=null || subjectIds !=null){
            return checkListEmptyGetResponse(
                    schoolYearSubjectRepository.findAllBySchoolYear_IdOrSubject_IdIn(schoolYearId,subjectIds),
                    "Không tìm thấy Môn học với schoolYearId: "+ schoolYearId +" Hoặc dánh sách subjectIds !!!"
            );
        }else{
            throw new RuntimeException("Cần ít nhất 1 trong các tham số sau [ id , schoolYearId , subjectIds ] !!!");
        }
    }
    public ResponseEntity<?> getTeacherSchoolYear(@Nullable Long id,@Nullable Long teacherId,@Nullable Long schoolYearId){
        if(id!=null){
            return ResponseEntity.ok(teacherSchoolYearRepository.findById(id)
                    .orElseThrow(()->new NullPointerException("Không tìm thấy Giáo Viên với ID = "+id+" !!!")));
        }
        if(teacherId!=null && schoolYearId !=null){
            return checkListEmptyGetResponse(
                    teacherSchoolYearRepository.findAllByTeacher_IdAndSchoolYear_Id(teacherId,schoolYearId),
                    "Không tìm thấy TeacherSchoolYear với teacherId: "+teacherId+" Và schoolYearId: "+schoolYearId+" !!!"
            );
        }
        if(teacherId!=null || schoolYearId !=null){
            return checkListEmptyGetResponse(
                    teacherSchoolYearRepository.findAllByTeacher_IdOrSchoolYear_Id(teacherId,schoolYearId),
                    "Không tìm thấy TeacherSchoolYear với teacherId: "+teacherId+" Hoặc schoolYearId: "+schoolYearId+" !!!"
            );
        }else {
            throw new RuntimeException("Cần ít nhất 1 trong các tham số sau [ id , teacherId , schoolYearId ] !!!");
        }
    }

    /**
     * @get list class by school year
     * @get list class by school year & grade
     * @get list class by school year & teacher
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
        if(schoolYearId!=null){
            if(gradeId!=null){
               return checkListEmptyGetResponse(
                       schoolYearClassRepository.findAllBySchoolYear_IdAndGrade_Id(schoolYearId,gradeId),
                "Không tìm thấy SchoolYearClass với schoolYearId:"+schoolYearId+" và gradeId: "+gradeId+" !!!"
               );
            }
            if(teacherSchoolYearId!=null) {
                return checkListEmptyGetResponse(
                        schoolYearClassRepository.findAllByTeacherSchoolYear_IdAndSchoolYear_Id(teacherSchoolYearId, schoolYearId),
                        "Không tìm thấy SchoolYearClass với schoolYearId:" + schoolYearId + " và teacherSchoolYearId: " + teacherSchoolYearId + " !!!"
                );
            }
            return checkListEmptyGetResponse(
                    schoolYearClassRepository.findAllBySchoolYear_Id(schoolYearId),
                    "Không tìm thấy SchoolYearClass với schoolYearId:"+schoolYearId+" !!!"
            );
        }
        var result = schoolYearClassRepository
                .findAllByIdOrClassNameOrClassCodeOrTeacherSchoolYear_IdOrSchoolYear_IdOrGrade_IdOrRoom_Id(
                        id,className,classCode,teacherSchoolYearId,schoolYearId,gradeId,roomId
                );
        if(result.size() > 0) throw new NullPointerException("Không tìm thấy SchoolYearClass !!!");
        return ResponseEntity.ok(result);
    }



    /**
     * @get subject by id
     * @get list subject by grade
     * @get list subject by grade between number
     * @get list subject by grade and sem
     * @return SchoolYearSubjectGrade or List<SchoolYearSubjectGrade>
     * @throws NullPointerException if not found
     */
    public ResponseEntity<?> getSchoolYearSubjectGrade(
            @Nullable Long id,
            @Nullable Long schoolYearSubjectId,
            @Nullable Long gradeId,
            @Nullable Integer number,
            @Nullable ESem sem
    ){
        if(gradeId!=null){
            if(sem!=null){
                return checkListEmptyGetResponse(
                    schoolYearSubjectGradeRepository.findAllByGrade_IdAndSemIsLike(gradeId,sem),
                    "Không tìm thấy SchoolYearSubjectGrade với gradeId: "+gradeId+" và sem: "+sem+" !!!"
                );
            }
            if(number!=null){
                return checkListEmptyGetResponse(
                    schoolYearSubjectGradeRepository.findAllByGrade_IdAndNumber(gradeId,number),
                    "Không tìm thấy SchoolYearSubjectGrade với gradeId: "+gradeId+" và number: "+number+" !!!"
                );
            }
            return checkListEmptyGetResponse(
                schoolYearSubjectGradeRepository.findAllByGrade_Id(gradeId),
                "Không tìm thấy SchoolYearSubjectGrade với gradeId: "+gradeId+" !!!"
            );
        }
        var result = schoolYearSubjectGradeRepository
            .findAllByIdOrSchoolYearSubject_Id(id,schoolYearSubjectId);
        if(result.size() > 0) throw new NullPointerException("Không tìm thấy SchoolYearSubjectGrade !!!");
        return  ResponseEntity.ok(result);
    }

    public ResponseEntity<?> getSchedule(@Nullable Long classId){
        var schoolYearClass = schoolYearClassRepository.findById(classId).orElseThrow(()->new NullPointerException("Không tìm thất Lớp với id: "+classId+"!!!"));
        return checkListEmptyGetResponse(
                scheduleRepository.findAllBySchoolYearClass(schoolYearClass),
                "Thời khóa biểu của classId: "+classId+" Rỗng!!!"
        );
    }

    /**
     * @description Kiểm tra số tiết học trong 1 năm học khi phân phối chương trình học
     * @return true - nếu số tiết học trong 1 năm học đã đủ
     * */
    private boolean checkPeriod(SchoolYear schoolYear,int addPeriod){
        var month = ChronoUnit.MONTHS.between(
                LocalDate.parse(schoolYear.getStartSem1().toString().substring(0,10)),
                LocalDate.parse(schoolYear.getEnd().toString().substring(0,10))
        );
        var numberPeriodOfYear = month*TONG_TIET_HOC_1_TUAN;
        var currentPeriod =  schoolYearSubjectGradeRepository.getPeriodOfSchoolYearAndGrade(1L,1L);
//        schoolYearSubjectGradeRepository
        return currentPeriod >= numberPeriodOfYear;
    }

    /**
     * @description Kiểm tra danh sách trống và trả về response
     * @throws NullPointerException nếu danh sách trống
     * @return ResponseEntity<?> nếu danh sách không trống
     * */
    private ResponseEntity<?> checkListEmptyGetResponse(List<?> data,String message){
        if(data.isEmpty()) throw new NullPointerException(message);
        return ResponseEntity.ok(data);
    }
}
