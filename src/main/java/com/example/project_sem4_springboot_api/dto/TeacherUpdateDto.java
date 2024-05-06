package com.example.project_sem4_springboot_api.dto;

import com.example.project_sem4_springboot_api.entities.Teacher;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.entities.UserDetail;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherUpdateDto extends TeacherDetailsDto {
    @NotNull(message = "Id không được để trống!!!")
    private Long id;
    private boolean active;
    @Override
    public Teacher toTeacher(User user) {
        return Teacher.builder().id(id).active(isActive()).sortName(getSortName()).officerNumber(getOfficerNumber()).joiningDate(getJoiningDate()).user(user).build();
    }

    @Override
    public UserDetail toUserDetail(User user) {
//        UserDetail newUD = super.toUserDetail(user);
//        newUD.setId(getId());
        return super.toUserDetail(user);
    }
}
