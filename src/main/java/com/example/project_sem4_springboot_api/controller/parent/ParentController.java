package com.example.project_sem4_springboot_api.controller.parent;

import com.example.project_sem4_springboot_api.dto.StudentParentCreateDto;
import com.example.project_sem4_springboot_api.dto.TeacherDetailsDto;
import com.example.project_sem4_springboot_api.dto.UserDetailDto;
import com.example.project_sem4_springboot_api.entities.request.CreateParent;
import com.example.project_sem4_springboot_api.service.impl.StudentParentServiceImpl;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/parent")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ParentController {
    /*
    * create parent (parent detail & student parent)
    * read: student info at current year
    * read: teacher contact of student
    * */

    StudentParentServiceImpl studentParentService;

    @PostMapping("/create-parent")
    public ResponseEntity<?> createParent(@RequestBody StudentParentCreateDto data){
        return ResponseEntity.status(HttpStatus.CREATED).body(studentParentService.createStudentParent(data));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getParent(@Nullable @PathVariable Long id){
        return studentParentService.getParent(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteParent(@Nullable @PathVariable Long id){
        return studentParentService.deleteParent(id);
    }
}
