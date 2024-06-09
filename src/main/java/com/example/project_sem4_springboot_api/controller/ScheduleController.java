package com.example.project_sem4_springboot_api.controller;
import com.example.project_sem4_springboot_api.entities.enums.DayOfWeek;
import com.example.project_sem4_springboot_api.entities.request.*;
import com.example.project_sem4_springboot_api.exception.ArgumentNotValidException;
import com.example.project_sem4_springboot_api.service.impl.ScheduleServiceImpl;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleServiceImpl scheduleService;

    @GetMapping()
    public ResponseEntity<?> get_Schedule(
            @Nullable @RequestParam Long classId,
            @Nullable @RequestParam Long teacherSchoolYearId,
            @Nullable @RequestParam Long gradeId,
            @Nullable @RequestParam Long schoolYearId){
        return scheduleService.getSchedule(classId,teacherSchoolYearId,gradeId,schoolYearId);
    }
    @GetMapping("/get-schedule-by")
    public ResponseEntity<?> get_ScheduleBy(
            @RequestParam Long calendarId,
            @Nullable @RequestParam Long classId,
            @Nullable @RequestParam Long teacherSchoolYearId
    ){
        return scheduleService.getSchedule2(calendarId,classId,teacherSchoolYearId);
    }

    @GetMapping("/get-calendar-release")
    public  ResponseEntity<?> get_CalendarRelease(@Nullable @RequestParam Long Id, @Nullable @RequestParam Long schoolYearId){
        return scheduleService.getCalendarRelease(Id,schoolYearId);
    }
    @GetMapping("/get-teacher-class-subject")
    public  ResponseEntity<?> get_TeacherSubjectClass( @RequestParam Long classId){
        return scheduleService.getTeacherSubjectClass(classId);
    }
    @PostMapping("/create-schedule")
    public ResponseEntity<?> createSchedule (@Valid @RequestBody ScheduleCreate data){
        return scheduleService.createSchedule(data);
    }

    @PostMapping("/create-calendar-release")
    public ResponseEntity<?> createCalendarRelease(@Valid @RequestBody CalendarReleaseCreate data){
        return scheduleService.createCalendarRelease(data);
    }

    @PutMapping("/update-schedule")
    public ResponseEntity<?> updateSchedule (@Valid @RequestBody ScheduleUpdate data){
        if(data.getId()!=null){
            return scheduleService.updateSchedule(data);
        }
        throw new ArgumentNotValidException("Id Tkb không được để trống!!!","Id","null");
    }

    @PutMapping("/apply-calendar-release")
    public ResponseEntity<?> apply_CalendarRelease (@RequestParam Long calendarId){
        return scheduleService.updateCalendarRelease(calendarId);
    }

    @PutMapping("/update-calendar-release")
    public ResponseEntity<?> update_CalendarRelease (@Valid @RequestBody CalendarReleaseUpdate data){
        return scheduleService.updateCalendarRelease(data);
    }

    @DeleteMapping("/delete-schedule")
    private  ResponseEntity<?> deleteSchedule(@Nullable @RequestParam Long Id,@Nullable List<Long> Ids){
        return scheduleService.deleteSchedule(Id,Ids);
    }
    @DeleteMapping("/delete-calendar-release")
    private  ResponseEntity<?> delete_CalendarRelease(@Nullable @RequestParam Long Id){
        return scheduleService.deleteCalendarRelease(Id);
    }

    @GetMapping("/check-lesson-exist")
    public ResponseEntity<?> getScheduleByTeacher(
            @RequestParam Long teacherSchoolYearId,
            @RequestParam DayOfWeek dow,
            @RequestParam int indexLesson
    ){
        return scheduleService.checkLessonExist(teacherSchoolYearId,dow,indexLesson);
    }
}
