package com.example.project_sem4_springboot_api.dto;

import com.example.project_sem4_springboot_api.entities.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

    @NotNull(message = "Giới tính không được để trống!!!")
    private boolean gender;

    @NotBlank(message = "Tên không được để trống!!!")
    @Size(min = 2, max = 50, message = "Tên phải từ 2 đến 50 ký tự!!!")
    private String firstName;

    @NotBlank(message = "Họ không được để trống!!!")
    @Size(min = 2, max = 50, message = "Tên phải từ 2 đến 50 ký tự!!!")
    private String lastName;

    @NotNull(message = "Ngày sinh không được để trống!!!")
    private Date birthday;

    private String address;

    @NotBlank(message = "Mã sinh viên không được để trống!!!")
    @Size(min = 2, max = 50, message = "Mã sinh viên phải từ 2 đến 50 ký tự!!!")
    private String studentCode;

    @NotNull(message = "Id Lớp không được để trống!!!")
    private Long schoolYearClassId;


    @JsonIgnore
    public Student toStudent(){
        return Student.builder().gender(gender).firstName(firstName).lastName(lastName).birthday(birthday).address(address)
                .studentCode(studentCode).createdAt(new Date(System.currentTimeMillis())).build();
    }
}
