package com.example.project_sem4_springboot_api.dto;

import com.example.project_sem4_springboot_api.entities.Employee;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.entities.request.RegisterRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class EmployeeCreateDto extends RegisterRequest {

    @NotBlank(message = "Số hiệu employee không được để trống!!!")
    private String officerNumber;
    @NotNull(message = "Ngày vào làm không được để trống!!!")
    private Date joiningDate;

    @NotBlank(message = "Tên ngắn không được để trống!!!")
    private String sortName;
    @JsonIgnore
    public Employee toEmployee(User user){
        return Employee.builder()
                .active(true)
                .officerNumber(officerNumber)
                .sortName(sortName)
                .joiningDate(joiningDate)
                .user(user)
                .build();
    }

}
