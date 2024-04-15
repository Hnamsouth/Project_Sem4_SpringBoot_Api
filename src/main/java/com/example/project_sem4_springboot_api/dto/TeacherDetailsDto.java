package com.example.project_sem4_springboot_api.dto;

import com.example.project_sem4_springboot_api.entities.Teacher;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.entities.request.RegisterRequest;
import com.example.project_sem4_springboot_api.entities.response.TeacherResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDetailsDto extends RegisterRequest {
    @NotBlank(message = "Số hiệu giáo viên không được để trống!!!")
    private String officerNumber;
    @NotNull(message = "Ngày vào làm không được để trống!!!")
    private Date joiningDate;

    @NotBlank(message = "Tên ngắn không được để trống!!!")
    private String sortName;
    @JsonIgnore
    public Teacher toTeacher(User user){
        return Teacher.builder().active(true).officerNumber(officerNumber).sortName(sortName).joiningDate(joiningDate).user(user).build();
    }
}
