package com.example.project_sem4_springboot_api.controller.student;


import com.example.project_sem4_springboot_api.entities.enums.ESem;
import com.example.project_sem4_springboot_api.entities.request.StudentScoreCreate;
import com.example.project_sem4_springboot_api.service.impl.StudentScoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student-score")
@RequiredArgsConstructor
public class StudentScoreController {

    private final StudentScoreService studentScoreService;

    @GetMapping("/get-class-list-entrusted")
    public ResponseEntity<?> getClassListEntrusted(@RequestParam Long schoolYearId,ESem sem){
        return studentScoreService.getClassByTeacher(schoolYearId,sem);
    }

    @GetMapping("/get-student-score-subject")
    public ResponseEntity<?> getStudentScoreSubject(
            @RequestParam ESem sem,
            @RequestParam Long schoolYearSubjectId,
            @RequestParam Long schoolYearClassId
    ){
        return studentScoreService.getStudentScoreSubject(sem, schoolYearSubjectId, schoolYearClassId);
    }

    @GetMapping("/get-student-score-subject-by")
    public ResponseEntity<?> getStudentScoreSubjectBy(
            @RequestParam(required = false) ESem sem,
            @RequestParam  Long studentYearInfoId,
            @RequestParam(required = false) Long schoolYearSubjectId
    ){
        return studentScoreService.getStudentScoreSubjectBy(sem,studentYearInfoId, schoolYearSubjectId);
    }

    @GetMapping("/get-point-type")
    public ResponseEntity<?> getPointType(){
        return studentScoreService.getPointType();
    }

    @PostMapping("/create-points")
    public ResponseEntity<?> insertPoints(@RequestBody StudentScoreCreate data){
        return studentScoreService.createOrUpdateStudentScore(data);
    }

}
