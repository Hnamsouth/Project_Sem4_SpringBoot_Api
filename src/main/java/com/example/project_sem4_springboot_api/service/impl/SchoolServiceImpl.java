package com.example.project_sem4_springboot_api.service.impl;
import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.ESem;
import com.example.project_sem4_springboot_api.entities.request.*;
import com.example.project_sem4_springboot_api.exception.DataExistedException;
import com.example.project_sem4_springboot_api.repositories.*;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
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
            if(check.contains(sem1.get(Calendar.YEAR))) throw new DataExistedException("Năm học đã tồn tại!!!");
            var createdData =  schoolYearRepository.save(data.toSchoolYear());
            return new ResponseEntity<>(createdData,HttpStatus.CREATED);
        }catch (Exception e){
            throw  new RuntimeException(e.getMessage());
        }
    }
    public ResponseEntity<?> createSchoolYearSubject(SchoolYearSubjectCreate data){
        var schoolYear = schoolYearRepository.findById(data.getSchoolYearId()).orElseThrow(
                ()->new NullPointerException("Not Found School Year !!!")
        );
        if(data.getSubjectId() != null){
            if(schoolYearSubjectRepository.existsBySubject_Id(data.getSubjectId()))
                throw new DataExistedException("Môn học đã tồn tại !!!");
            var subject = subjectRepository.findById(data.getSubjectId()).orElseThrow(
                    ()->new NullPointerException("Subject Invalid !!!")
            );
            var createdData = schoolYearSubjectRepository.save(
                    SchoolYearSubject.builder().subject(subject).schoolYear(schoolYear).build());
            return new ResponseEntity<>(createdData,HttpStatus.CREATED);
        }
        if(!data.getSubjectIds().isEmpty()){
            var subjects = subjectRepository.findAllById(data.getSubjectIds());
            if(!subjects.isEmpty()){
                if(schoolYearSubjectRepository.existsBySubject_IdIn(data.getSubjectIds()))
                    throw new DataExistedException("Một số môn học đã tồn tại. Vui lòng kiểm tra lại");
                var createdData =  schoolYearSubjectRepository.saveAll(
                    subjects.stream().map((s)-> SchoolYearSubject.builder().subject(s).schoolYear(schoolYear).build()).toList()
                );
                return new ResponseEntity<>(createdData,HttpStatus.CREATED);
            }
            throw new NullPointerException("Không tìm thấy danh sách môn học !!. Kiểm tra lại subjectIds");
        }
        throw new NullPointerException("Yêu cầu subjectId hoặc subjectIds !!!");
    }
    public ResponseEntity<?> createTeacherSchoolYear(TeacherSchoolYearCreate data){
        var schoolYear = schoolYearRepository.findById(data.getTeacherId()).orElseThrow(
                ()->new NullPointerException("Không tìm thấy năm học!!!")
        );
        if(data.getTeacherId() != null ){
            if(teacherSchoolYearRepository.existsByTeacher_IdAndSchoolYear_Id(data.getTeacherId(),data.getSchoolYearId()))
                throw new DataExistedException("Giáo viên đã tồn tại trong năm học !!!");
            var teacher = teacherRepository.findById(data.getTeacherId()).orElseThrow(
                    ()->new NullPointerException("Không tìm thấy giáo viên !!!")
            );
            var createdData = teacherSchoolYearRepository.save(
                    TeacherSchoolYear.builder().teacher(teacher).schoolYear(schoolYear).build());
            return new ResponseEntity<>(createdData,HttpStatus.CREATED);
        }
        if(!data.getTeacherIds().isEmpty()){
            var teachers = teacherRepository.findAllById(data.getTeacherIds());
            if(!teachers.isEmpty()){
                if(teacherSchoolYearRepository.existsByTeacher_IdInAndSchoolYear_Id(data.getTeacherIds(),data.getSchoolYearId()))
                    throw new DataExistedException("Một số Giáo viên đã tồn tại trong năm học!. Vui lòng kiểm tra lại");
                var createdData =  teacherSchoolYearRepository.saveAll(
                        teachers.stream().map((t)-> TeacherSchoolYear.builder().teacher(t).schoolYear(schoolYear).build()).toList()
                );
                return new ResponseEntity<>(createdData,HttpStatus.CREATED);
            }
            throw new NullPointerException("Không tìm thấy danh sách giáo viên !!!");
        }
        throw new NullPointerException("Yêu cầu teacherId hoặc teacherIds !!!");
    }
    public ResponseEntity<?> createSchoolYearClass(SchoolYearClassCreate data){
        var schoolYear = schoolYearRepository.findById(data.getSchoolYear()).orElseThrow(
                ()->new NullPointerException("Không tìm thấy năm học !!!")
        );
        // check class name exist
        if(schoolYearClassRepository.existsByClassNameAndSchoolYear_Id(data.getClassName(),data.getSchoolYear()))
            throw new DataExistedException("Tên Lớp đã được sử dụng!!");
        // check class code exist
        if(schoolYearClassRepository.existsByTeacherSchoolYear_IdAndSchoolYear_Id(data.getTeacherSchoolYear(),data.getSchoolYear()))
            throw new DataExistedException("Giáo viên đã Chủ nhiệm lớp khác!!!");
        var createdData=  schoolYearClassRepository.save(
                SchoolYearClass.builder()
                        .className(data.getClassName()).classCode(data.getClassCode())
                        .teacherSchoolYear(teacherSchoolYearRepository.findById(data.getTeacherSchoolYear()).orElseThrow(
                                ()->new NullPointerException("Không tìm thấy Giáo viên !!!")
                        ))
                        .grade(gradeRepository.findById(data.getGradeId()).orElseThrow(
                                ()->new NullPointerException("Không tìm thấy Khối học !!!")
                        ))
                        .room(roomRepository.findById(data.getRoomId()).orElseThrow(
                                ()->new NullPointerException("Không tìm thấy Phòng học !!!")
                        ))
                        .schoolYear(schoolYear).build()
        );
        return new ResponseEntity<>(createdData,HttpStatus.CREATED);
    }
    public ResponseEntity<?> createSchoolYearSubjectGrade(SchoolYearSubjectGradeCreate data){
        // check subject exist
        var schoolYearSubject = schoolYearSubjectRepository.findById(data.getSchoolYearSubjectId()).orElseThrow(
                ()->new NullPointerException("Không tìm thấy Môn học !!!")
        );
        // check period of year
        if(checkPeriod(schoolYearSubject.getSchoolYear(),data.getNumber()))
            throw new RuntimeException("Số tiết học đã đầy!!!");
        var result =  schoolYearSubjectGradeRepository.save(
                SchoolYearSubjectGrade.builder()
                        .schoolYearSubject(schoolYearSubject)
                        .grade(gradeRepository.findById(data.getGradeId()).orElseThrow(
                                ()->new NullPointerException("Không tìm thấy Khối học !!!")
                        ))
                        .sem(data.getSem()).number(data.getNumber()).build());
        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }
    public ResponseEntity<?> createTeacherSchoolYearClassSubject(TeacherSchoolYearClassSubjectCreate data) {
        List<TeacherSchoolYearClassSubject> result = new ArrayList<>();
        var teacherSchoolYear = teacherSchoolYearRepository.findByTeacherId(data.getTeacherSchoolYearId());
        if(teacherSchoolYear == null)
            throw new NullPointerException("Không tìm thấy Giáo viên với id : "+data.getTeacherSchoolYearId()+"!!!");
        data.getSubjectClassList().forEach(e->{
            // check subject exist
            var schoolYearSubject = schoolYearSubjectRepository.findById(e.getSchoolYearSubjectId()).orElseThrow(
                ()->new NullPointerException("Không tìm thấy Môn học với Id: "+e.getSchoolYearSubjectId()+" !!!"));
            List<TeacherSchoolYearClassSubject> teacherSchoolYearClassSubjects = new ArrayList<>();
            e.getSchoolYearClassId().forEach(c->{
                // check subject exist
                var schoolYearClass =  schoolYearClassRepository.findById(c).orElseThrow(
                    ()->new NullPointerException("Không tìm thấy Lớp học với  Id: "+c+" !!!"));
                // check teacher teach subject to class yet
                if(teacherSchoolYearClassSubjectRepository.findByTeacherSchoolYear_IdAndSchoolYearClass_IdAndTeacherSchoolYear_Id(
                        teacherSchoolYear.getId(),c,e.getSchoolYearSubjectId()))
                    throw new NullPointerException("Giáo viên: "+teacherSchoolYear.getId()+" đã dạy subjectId: "+e.getSchoolYearSubjectId()+" ở lớp "+c+" !!!");
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
        var teacher = teacherSchoolYearRepository.findById(data.getTeacherSchoolYearId()).orElseThrow(
                ()-> new NullPointerException("Không tìm thấy giáo viên !!!"));
        var classs = schoolYearClassRepository.findById(data.getSchoolYearClassId()).orElseThrow(
                ()-> new NullPointerException("Không tìm thấy Lớp học !!!"));
        var subject = schoolYearSubjectRepository.findById(data.getSchoolYearSubjectId()).orElseThrow(
                ()-> new NullPointerException("Không tìm thấy Môn học !!!"));
        if(scheduleRepository.existsByDayOfWeekAndStudyTimeAndIndexLessonAndSchoolYearClass_Id(data.getDayOfWeek(),data.getStudyTime(),data.getIndexLesson(),data.getSchoolYearClassId()))
            throw new DataExistedException("Tiết học "+data.getIndexLesson()+" buổi "+data.getStudyTime()+" thứ "+data.getDayOfWeek().toString()+" của lớp "+classs.getClassName()+" đã tồn tại !!!");
        var createdData =  scheduleRepository.save(Schedule.builder().schoolYearSubject(subject).teacherSchoolYear(teacher)
                .schoolYearClass(classs).dayOfWeek(data.getDayOfWeek()).studyTime(data.getStudyTime())
                .indexLesson(data.getIndexLesson()).note(data.getNote()).build());
        return new ResponseEntity<>(createdData.toScheduleResponse(),HttpStatus.CREATED);
    }
    /**
    * 1: read subject
    * 2: read school year
    * 3: read school year class
    * 4: read schedule
    * */
    public ResponseEntity<?> getSubject(@Nullable Long id){
        if(id!=null) return ResponseEntity.ok(subjectRepository.findById(id).orElseThrow(()->new NullPointerException("Không tìm thấy Môn học  id: "+id+" !!!")));
        return ResponseEntity.ok(subjectRepository.findAll());
    }
    public ResponseEntity<?> getTeacher(@Nullable Long id){
        if(id !=null) return ResponseEntity.ok(teacherRepository.findById(id).orElseThrow(()->new NullPointerException("Không tìm thấy giáo viên id = "+id+"!!!")));
        return ResponseEntity.ok(teacherRepository.findAll());
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
        }
        throw new RuntimeException("Cần ít nhất 1 trong các tham số sau [ id , schoolYearId , subjectIds ] !!!");
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
            @Nullable Long id,@Nullable Long gradeId,@Nullable Long schoolYearId,@Nullable Long teacherSchoolYearId,
            @Nullable Long roomId,@Nullable String className,@Nullable String classCode
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
            @Nullable Long id,@Nullable Long schoolYearSubjectId,@Nullable Long gradeId,
            @Nullable Integer number,@Nullable ESem sem
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

    /**
     * get thời khóa biêu
     *
     * @get lấy theo lớp
     * @get lấy theo giáo viên
     * @get lấy theo khối
     * @get lây theo năm học
     *
     * */
    public ResponseEntity<?> getSchedule(@Nullable Long classId,@Nullable Long teacherSchoolYearId,@Nullable Long gradeId,@Nullable Long schoolYearId){
        if(classId!=null){
            var schoolYearClass = schoolYearClassRepository.findById(classId).orElseThrow(()->new NullPointerException("Không tìm thấy Lớp với id: "+classId+"!!!"));
            return checkListEmptyGetResponse(scheduleRepository.findAllBySchoolYearClass(schoolYearClass).stream().map(Schedule::toScheduleResponse).toList(),
                    "Thời khóa biểu của classId: "+classId+" Rỗng !!!");
        }
        if(teacherSchoolYearId!=null || gradeId!=null || schoolYearId != null){
            String mess = teacherSchoolYearId!=null ? "teacherSchoolYearId: "+teacherSchoolYearId :
                    gradeId!=null ? "gradeId:"+gradeId:"schoolYearId:"+schoolYearId;
            return checkListEmptyGetResponse(
                scheduleRepository.findAllBySchoolYearClass_Grade_IdOrSchoolYearClass_SchoolYear_IdOrTeacherSchoolYear_Id(gradeId,schoolYearId,teacherSchoolYearId).stream().map(Schedule::toScheduleResponse).toList(),
                "Thời khóa biểu của "+mess+" Rỗng !!!");
        }
        throw new RuntimeException("Yêu cầu cần Id của Lớp || Giáo viên || Khối || Năm học ");
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
