package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.TakeLeave;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


@Data
public class TakeLeaveRes extends TakeLeave {
    private StudentResponse studentInfo;


}
