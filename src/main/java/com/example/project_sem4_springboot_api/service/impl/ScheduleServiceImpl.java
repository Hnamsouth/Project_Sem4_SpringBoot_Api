package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.enums.DayOfWeek;
import com.example.project_sem4_springboot_api.entities.enums.StudyTime;
import com.example.project_sem4_springboot_api.entities.request.*;
import com.example.project_sem4_springboot_api.entities.response.ScheduleRes;
import com.example.project_sem4_springboot_api.exception.ArgumentNotValidException;
import com.example.project_sem4_springboot_api.exception.DataExistedException;
import com.example.project_sem4_springboot_api.repositories.*;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.project_sem4_springboot_api.service.impl.SchoolServiceImpl.checkListEmptyGetResponse;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl {

    private final TeacherSchoolYearRepository teacherSchoolYearRepository;
    private final TeacherSchoolYearClassSubjectRepository teacherSchoolYearClassSubjectRepository;
    private final SchoolYearSubjectRepository schoolYearSubjectRepository;
    private final SchoolYearClassRepository schoolYearClassRepository;
    private final SchoolYearRepository schoolYearRepository;
    private final ScheduleRepository scheduleRepository;
    private final CalendarReleaseRepository calendarReleaseRepository;


    public ResponseEntity<?> getTeacherSubjectClass(Long classId){
        var res = teacherSchoolYearClassSubjectRepository.findAllBySchoolYearClass_Id(classId);
        return ResponseEntity.ok(res.stream().map(TeacherSchoolYearClassSubject::toRes).toList());
    }

    public ResponseEntity<?> createSchedule(ScheduleCreate data) {
        var tcs = teacherSchoolYearClassSubjectRepository.findAllByIdIn(
                data.getScheduleDetailCreate().stream().map(ScheduleDetailCreate::getTeacherSchoolYearClassSubjectId).toList()
        );
        if(tcs.isEmpty()) throw new NullPointerException("Id Phân công giảng dạy không hợp lệ !!!");
        CalendarRelease calendarRelease = calendarReleaseRepository.findById(data.getCalendarReleaseId()).orElseThrow(
                ()->new NullPointerException("Không tìm thấy Đợt áp dụng với id: "+data.getCalendarReleaseId()+" !!!")
        );
        var schoolYearClass = schoolYearClassRepository.findById(data.getClassId()).orElseThrow(
                ()->new NullPointerException("Không tìm thấy Lớp học với id: "+data.getClassId()+" !!!")
        );
        var scheduleList = data.getScheduleDetailCreate().stream().map(e->{
            //  ktra tiết học đã tồn tại chưa
            checkLessonExist(e.getDayOfWeek(),e.getStudyTime(),e.getIndexLesson(),schoolYearClass.getId());
            var t =tcs.stream().filter(c->c.getId().equals(e.getTeacherSchoolYearClassSubjectId())).findAny().orElseThrow();
            return Schedule.builder()
                    .schoolYearSubject(t.getSchoolYearSubject())
                    .teacherSchoolYear(t.getTeacherSchoolYear())
                    .schoolYearClass(t.getSchoolYearClass())
                    .calendarRelease(calendarRelease)
                    .dayOfWeek(e.getDayOfWeek())
                    .studyTime(e.getStudyTime())
                    .indexLesson(e.getIndexLesson())
                    .note(e.getNote())
                    .build();
        }).toList();

        // check releaseAt after startSem1 and before endSem2
        checkReleaseAt(calendarRelease.getReleaseAt(),calendarRelease.getSchoolYear());
        var createdData =  scheduleRepository.saveAll(scheduleList);

        return new ResponseEntity<>(toScheduleRes(createdData), HttpStatus.CREATED);
    }
    public ResponseEntity<?> createCalendarRelease(CalendarReleaseCreate data) {
        var schoolYear = schoolYearRepository.findById(data.getSchoolYearId()).orElseThrow(()->new NullPointerException("Không tìm thấy Năm học với id: "+data.getSchoolYearId()+" !!!"));
        checkReleaseAt(data.getReleaseAt(),schoolYear);
        var createData = calendarReleaseRepository.save(
                CalendarRelease.builder()
                        .title(data.getTitle())
                        .releaseAt(data.getReleaseAt())
                        .schoolYear(schoolYear)
                        .status(false)
                        .statusName("Chưa áp dụng")
                        .build()
        );
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

    public ResponseEntity<?> getSchedule2(Long calendarId,@Nullable Long classId,@Nullable Long teacherSchoolYearId){
        if(classId!=null){
            var schoolYearClass = schoolYearClassRepository.findById(classId).orElseThrow(()->new NullPointerException("Không tìm thấy Lớp với id: "+classId+"!!!"));
            var listSchedule = scheduleRepository.findAllBySchoolYearClassAndCalendarRelease_Id(schoolYearClass,calendarId);
            return checkListEmptyGetResponse(toScheduleRes(listSchedule),
                    "Thời khóa biểu của classId: "+classId+" Rỗng !!!");
        }
        throw new RuntimeException("Yêu cầu cần Id của Lớp || Giáo viên || Khối || Năm học ");
    }
    public ResponseEntity<?> getCalendarRelease(Long id,Long schoolYearId){
        if(id!=null) return ResponseEntity.ok(calendarReleaseRepository.findById(id).orElseThrow(()->new NullPointerException("Không tìm thấy CalendarRelease với id: "+id+" !!!")).toResponse());
        if(schoolYearId!=null) {
           var res = calendarReleaseRepository.findAllBySchoolYear_Id(schoolYearId);
           if(res.isEmpty()) throw new NullPointerException("Không tìm thấy CalendarRelease với schoolYearId: "+schoolYearId+" !!!");
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
    public ResponseEntity<?> updateCalendarRelease(Long calendarReleaseId){
        var calendarRelease = calendarReleaseRepository.findById(calendarReleaseId)
                .orElseThrow(()->new NullPointerException("Không tìm thấy Đợt áp dụng TKB với id: "+calendarReleaseId+" !!!"));
        if(calendarRelease.isStatus()) return ResponseEntity.ok("Đợt áp dụng này đã được áp dụng !!!");
        calendarRelease.setStatus(true);
        calendarRelease.setStatusName("Đã áp dụng");
        var setF =  calendarReleaseRepository.findAllBySchoolYear_Id(calendarRelease.getSchoolYear().getId());
        var result = new ArrayList<>(setF.stream().map(e -> {
            if (!e.getId().equals(calendarReleaseId)) {
                e.setStatus(false);
                e.setStatusName("Chưa áp dụng");
            }
            return e;
        }).toList());
        result.add(calendarRelease);
        var updateData = calendarReleaseRepository.saveAll(result);
        // thong bao cho phu huynh hs vaf giao vien


        return ResponseEntity.ok("Áp dụng Thành công !!!");
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
    public ResponseEntity<?> deleteCalendarRelease(Long id){
        var deleteData = calendarReleaseRepository.findById(id).orElseThrow(()->new NullPointerException("Không tìm thấy Đợt áp dụng TKB với id: "+id+" !!!"));
        calendarReleaseRepository.delete(deleteData);
        return ResponseEntity.ok("Xóa Thành công !!!");
    }

    public ResponseEntity<?> checkLessonExist(Long teacherId,DayOfWeek dow, int indexLesson){
        var check = scheduleRepository.existsByDayOfWeekAndIndexLessonAndTeacherSchoolYear_Id(dow,indexLesson,teacherId);
        return ResponseEntity.ok(check);
    }

    private List<ScheduleRes> toScheduleRes(List<Schedule> data){
        List<ScheduleRes> res = new ArrayList<>();
        for(int tiethoc=1; tiethoc <= 8; tiethoc++){
            int finalTiethoc = tiethoc;
            var list = data.stream().filter(e->e.getIndexLesson()== finalTiethoc).toList();
            ScheduleRes scheduleRes =  ScheduleRes.builder().build();
            list.forEach(e->{
                switch (e.getDayOfWeek()){
                    case T2 -> scheduleRes.setT2(e.toScheduleResponse());
                    case T3 -> scheduleRes.setT3(e.toScheduleResponse());
                    case T4 -> scheduleRes.setT4(e.toScheduleResponse());
                    case T5 -> scheduleRes.setT5(e.toScheduleResponse());
                    case T6 -> scheduleRes.setT6(e.toScheduleResponse());
                }
            });
            res.add(scheduleRes);
        }
        return res;
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
        if(releaseAt.before(schoolYear.getStartSem1()) || releaseAt.after(schoolYear.getEnd()))
            throw new ArgumentNotValidException("Ngày Áp dụng phải sau ngày bắt đầu học kỳ 1 và trước ngày kết thúc học kỳ 2 !!!","releaseAt",releaseAt.toString());
    }
    private void checkLessonExist(DayOfWeek dow, StudyTime st, int indexLesson,Long schoolYearCLassId)throws DataExistedException{
        if(scheduleRepository.existsByDayOfWeekAndStudyTimeAndIndexLessonAndSchoolYearClass_Id(dow,st,indexLesson,schoolYearCLassId))
            throw new DataExistedException("Tiết học "+indexLesson+" buổi "+st+" thứ "+dow.toString()+" của lớp "+schoolYearCLassId+" đã tồn tại !!!");
    }
}
