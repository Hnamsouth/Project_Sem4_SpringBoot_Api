package com.example.project_sem4_springboot_api.controller.student;


import com.example.project_sem4_springboot_api.entities.enums.ESem;
import com.example.project_sem4_springboot_api.entities.request.StudyResultCU;
import com.example.project_sem4_springboot_api.service.impl.StudyResultService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student-study-result")
@RequiredArgsConstructor
public class StudentStudyResultController {


    private final StudyResultService studyResultService;


    @GetMapping("/get-class-list-entrusted")
    public ResponseEntity<?> getClassListEntrusted(
            @RequestParam Long schoolYearId,
            @RequestParam ESem sem,
            @RequestParam (required= false) List<Long>  gradeIds
    ){
        return studyResultService.getStudyResultClass(schoolYearId,sem,gradeIds);
    }

    @GetMapping("")
    public ResponseEntity<?> getClassListEntrusted(
            @RequestParam(required = false) Long schoolYearClassId,
            @RequestParam ESem sem,
            @RequestParam(required = false) Long studentYearInfoId
    ){
        return studyResultService.getStudyResult(sem,schoolYearClassId,studentYearInfoId);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createStudyResult(@Valid @RequestBody StudyResultCU data){
        return studyResultService.createStudyResult(data);
    }

}
