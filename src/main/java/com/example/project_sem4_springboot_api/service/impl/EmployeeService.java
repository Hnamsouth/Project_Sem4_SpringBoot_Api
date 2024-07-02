package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.constants.StatusData;
import com.example.project_sem4_springboot_api.dto.EmployeeCreateDto;
import com.example.project_sem4_springboot_api.entities.*;
import com.example.project_sem4_springboot_api.entities.response.ResultPaginationDto;
import com.example.project_sem4_springboot_api.repositories.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmployeeService {

    UserDetailRepository userDetailRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    StatusRepository statusRepository;
    EmployeeRepository employeeRepository;

    public ResponseEntity<?> createEmployee(EmployeeCreateDto data){
        Set<Role> roles = roleRepository.findByIdIn(data.getRole());
        if (roles.isEmpty()) throw new NullPointerException("Role not found");
        if (userRepository.existsByUsername(data.getUsername())) throw new NullPointerException("Username already exists");
        Status status = statusRepository.findByCode(StatusData.CREATE_NEW_USER);
        var user = User.builder().username(data.getUsername()).password(passwordEncoder.encode(data.getPassword()))
                .realPassword(data.getPassword()).roles(roles).status(status).createdAt(new java.sql.Date(System.currentTimeMillis())).build();
        var newUser = userRepository.save(user);
        var userDetail = userDetailRepository.save(data.toUserDetail(user));
        newUser.setUserDetail(List.of(userDetail));
        var newEmployee = employeeRepository.save(data.toEmployee(newUser));
        return new ResponseEntity<>(newEmployee, HttpStatus.CREATED);
    }

    public ResultPaginationDto getAllEmployees(Specification<Employee> specification, Pageable pageable){
        Page<Employee> employeePage = employeeRepository.findAll(specification, pageable);
        ResultPaginationDto resultPaginationDto = new ResultPaginationDto();
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber());
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(employeePage.getTotalPages());
        meta.setTotal(employeePage.getTotalElements());
        resultPaginationDto.setMeta(meta);
        resultPaginationDto.setResult(employeePage.getContent());
        return resultPaginationDto;
    }

    public ResponseEntity<?> getEmployeeById(Long id){
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isPresent()) {
            return new ResponseEntity<>(employee.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> updateEmployee(Employee data) {
        if(!employeeRepository.existsById(data.getId())) throw new NullPointerException("Không tìm thấy employee!!!");
        return ResponseEntity.ok(employeeRepository.save(data));
    }

    public void deleteEmployee(Long id){
        employeeRepository.deleteById(id);
    }

    public Employee getEmployeeById(long id){
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isEmpty()){
            throw new NullPointerException("Employee not found");
        }
        Employee employee = employeeOptional.get();
        return employee;
    }

}
