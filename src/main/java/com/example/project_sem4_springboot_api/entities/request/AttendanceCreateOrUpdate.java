package com.example.project_sem4_springboot_api.entities.request;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Builder
@Data
public class AttendanceCreateOrUpdate {
    public Date dayOff;
    public Long classId;
    public List<AttendanceBody> listStudent;
}
