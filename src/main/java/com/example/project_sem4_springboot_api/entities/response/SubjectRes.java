package com.example.project_sem4_springboot_api.entities.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SubjectRes {
    public Long id;
    public String name;
}
