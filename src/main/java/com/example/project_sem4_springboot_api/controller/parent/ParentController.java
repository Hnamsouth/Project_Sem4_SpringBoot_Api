package com.example.project_sem4_springboot_api.controller.parent;

import com.example.project_sem4_springboot_api.entities.request.CreateParent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/parent")
@RequiredArgsConstructor
public class ParentController {
    /*
    * create parent (parent detail & student parent)
    * read: student info at current year
    * read: teacher contact of student
    * */

    @PostMapping("/create-parent")
    public ResponseEntity<?> createParent(@Valid CreateParent data){
        return null;
    }
}
