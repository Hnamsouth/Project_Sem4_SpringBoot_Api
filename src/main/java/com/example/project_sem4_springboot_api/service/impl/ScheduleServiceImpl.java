package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.DayOfWeek;
import com.example.project_sem4_springboot_api.entities.enums.StudyTime;
import com.example.project_sem4_springboot_api.entities.request.CalendarReleaseCreate;
import com.example.project_sem4_springboot_api.entities.request.CalendarReleaseUpdate;
import com.example.project_sem4_springboot_api.entities.request.ScheduleCreate;
import com.example.project_sem4_springboot_api.entities.request.ScheduleUpdate;
import com.example.project_sem4_springboot_api.exception.ArgumentNotValidException;
import com.example.project_sem4_springboot_api.exception.DataExistedException;
import com.example.project_sem4_springboot_api.exception.ResourceNotFoundException;
import com.example.project_sem4_springboot_api.repositories.*;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.example.project_sem4_springboot_api.service.impl.SchoolServiceImpl.checkListEmptyGetResponse;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl {

    private final TeacherSchoolYearRepository teacherSchoolYearRepository;
    private final SchoolYearSubjectRepository schoolYearSubjectRepository;
    private final SchoolYearClassRepository schoolYearClassRepository;
    private final SchoolYearRepository schoolYearRepository;
    private final ScheduleRepository scheduleRepository;
    private final CalendarReleaseRepository calendarReleaseRepository;


    public ResponseEntity<?> createSchedule(ScheduleCreate data) {
        TeacherSchoolYear teacher = (TeacherSchoolYear) checkData(data.getTeacherSchoolYearId(),1);
        SchoolYearClass classs = (SchoolYearClass) checkData(data.getSchoolYearClassId(),2);
        SchoolYearSubject subject = (SchoolYearSubject) checkData(data.getSchoolYearSubjectId(),3);
        CalendarRelease calendarRelease = (CalendarRelease) checkData(data.getCalendarReleaseId(),5);
        //  ktra tiết học đã tồn tại chưa
        checkLessonExist(data.getDayOfWeek(),data.getStudyTime(),data.getIndexLesson(),data.getSchoolYearClassId());

        // check releaseAt after startSem1 and before endSem2
        var createdData =  scheduleRepository.save(Schedule.builder().schoolYearSubject(subject).teacherSchoolYear(teacher).calendarRelease(calendarRelease)
                .schoolYearClass(classs).dayOfWeek(data.getDayOfWeek()).studyTime(data.getStudyTime())
                .indexLesson(data.getIndexLesson()).note(data.getNote()).build());
        return new ResponseEntity<>(createdData.toScheduleResponse(), HttpStatus.CREATED);
    }
    public ResponseEntity<?> createCalendarRelease(CalendarReleaseCreate data) {
        var schoolYear = schoolYearRepository.findById(data.getSchoolYearId()).orElseThrow(()->new NullPointerException("Không tìm thấy Năm học với id: "+data.getSchoolYearId()+" !!!"));
        checkReleaseAt(data.getReleaseAt(),schoolYear);
        var createData = calendarReleaseRepository.save(CalendarRelease.builder().title(data.getTitle()).releaseAt(data.getReleaseAt()).schoolYear(schoolYear).build());
        return new ResponseEntity<>(createData,HttpStatus.CREATED);
    }
    /**
     * Thời khóa biêu
     *
     * @get lấy theo lớp - giáo viên - khối - năm học
     * */
    public ResponseEntity<?> getSchedule(@Nullable Long classId, @Nullable Long teacherSchoolYearId, @Nullable Long gradeId, @Nullable Long schoolYearId){
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
    public ResponseEntity<?> getCalendarRelease(Long id,Long schoolYearId){
        if(id!=null) return ResponseEntity.ok(calendarReleaseRepository.findById(id).orElseThrow(()->new NullPointerException("Không tìm thấy CalendarRelease với id: "+id+" !!!")).toResponse());
        if(schoolYearId!=null) {
           var res = calendarReleaseRepository.findAllBySchoolYear_Id(schoolYearId);
           if(res.isEmpty()) throw new ResourceNotFoundException("Không tìm thấy CalendarRelease với schoolYearId: "+schoolYearId+" !!!");
           return ResponseEntity.ok(res.stream().map(CalendarRelease::toResponse).toList());
        }
        return ResponseEntity.ok(calendarReleaseRepository.findAll().stream().map(CalendarRelease::toResponse).toList());
    }
    public ResponseEntity<?> updateSchedule(ScheduleUpdate data){
        var oldData = scheduleRepository.findById(data.getId()).orElseThrow(()->new NullPointerException("Không tìm thấy Thời khóa biểu với id: "+data.getId()+" !!!"));
        TeacherSchoolYear teacher = (TeacherSchoolYear) checkData(data.getTeacherSchoolYearId(),1);
        SchoolYearClass classs = (SchoolYearClass) checkData(data.getSchoolYearClassId(),2);
        SchoolYearSubject subject = (SchoolYearSubject) checkData(data.getSchoolYearSubjectId(),3);
        //  ktra tiết học đã tồn tại chưa
        if(oldData.getIndexLesson()!=data.getIndexLesson() || oldData.getStudyTime()!=data.getStudyTime() || oldData.getDayOfWeek()!=data.getDayOfWeek()){
            checkLessonExist(data.getDayOfWeek(),data.getStudyTime(),data.getIndexLesson(),data.getSchoolYearClassId());
        }
        oldData.setTeacherSchoolYear(teacher);
        oldData.setSchoolYearClass(classs);
        oldData.setSchoolYearSubject(subject);
        oldData.setDayOfWeek(data.getDayOfWeek());
        oldData.setStudyTime(data.getStudyTime());
        oldData.setIndexLesson(data.getIndexLesson());
        oldData.setNote(data.getNote());
        var updateData = scheduleRepository.save(oldData);
        return ResponseEntity.ok(updateData.toScheduleResponse());
    }
    public ResponseEntity<?> updateCalendarRelease(CalendarReleaseUpdate data){
        var calendarRelease = calendarReleaseRepository.findById(data.getId()).orElseThrow(()->new NullPointerException("Không tìm thấy Đợt áp dụng TKB với id: "+data.getId()+" !!!"));
        checkReleaseAt(data.getReleaseAt(),calendarRelease.getSchoolYear());
        calendarRelease.setTitle(data.getTitle());
        calendarRelease.setReleaseAt(data.getReleaseAt());
        var updateData =  calendarReleaseRepository.save(calendarRelease);
        return ResponseEntity.ok(updateData);
    }
    public ResponseEntity<?> deleteSchedule(Long id, List<Long> ids){
        scheduleRepository.deleteById(id);
       return ResponseEntity.ok("Xóa Thành công !!!");
    }
    private Object checkData(Long id,int index) throws NullPointerException,DataExistedException {
        if(index==1){
            return teacherSchoolYearRepository.findById(id).orElseThrow(()-> new NullPointerException("Không tìm thấy giáo viên với id = "+id+"!!!"));
        }else if(index ==2){
            return schoolYearClassRepository.findById(id).orElseThrow(()-> new NullPointerException("Không tìm thấy Lớp học với id = "+id+"!!!"));
        }else if(index==3){
           return schoolYearSubjectRepository.findById(id).orElseThrow(()-> new NullPointerException("Không tìm thấy Môn học với id = "+id+"!!!"));
        }else if(index==5){
            return calendarReleaseRepository.findById(id).orElseThrow(()->new NullPointerException("Không tìm thấy Đợt áp dụng thời khóa biểu với id = "+id+" !!!"));
        }
        return null;
    }
    private void checkReleaseAt(Date releaseAt,SchoolYear schoolYear )throws ArgumentNotValidException {
        if(releaseAt.after(schoolYear.getStartSem1()) || releaseAt.before(schoolYear.getEnd()))
            throw new ArgumentNotValidException("Ngày Áp dụng phải sau ngày bắt đầu học kỳ 1 và trước ngày kết thúc học kỳ 2 !!!","releaseAt",releaseAt.toString());
    }
    private void checkLessonExist(DayOfWeek dow, StudyTime st, int indexLesson,Long schoolYearCLassId)throws DataExistedException{
        if(scheduleRepository.existsByDayOfWeekAndStudyTimeAndIndexLessonAndSchoolYearClass_Id(dow,st,indexLesson,schoolYearCLassId))
            throw new DataExistedException("Tiết học "+indexLesson+" buổi "+st+" thứ "+dow.toString()+" của lớp "+schoolYearCLassId+" đã tồn tại !!!");
    }
}
