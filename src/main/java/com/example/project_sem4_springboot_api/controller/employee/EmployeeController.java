package com.example.project_sem4_springboot_api.controller.employee;

import com.example.project_sem4_springboot_api.dto.EmployeeCreateDto;
import com.example.project_sem4_springboot_api.entities.Employee;
import com.example.project_sem4_springboot_api.service.impl.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/employees")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeCreateDto data){
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(data));
    }

    @PutMapping("/employees")
    public ResponseEntity<?> updateEmployee(@RequestBody Employee data){
        return ResponseEntity.ok().body(employeeService.updateEmployee(data));
    }

    

}
