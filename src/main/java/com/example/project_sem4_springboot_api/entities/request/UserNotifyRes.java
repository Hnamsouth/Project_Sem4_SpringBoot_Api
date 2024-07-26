package com.example.project_sem4_springboot_api.entities.request;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Builder
@Data
public class UserNotifyRes {
    public List<String> tokens;
    public Map<String,String> data;
    public String title;
    public String body;
    public String image;
}
