package com.example.project_sem4_springboot_api.entities.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Notifications {
    private NTF_Type type;
    private String content;
    private String sender;

    public enum NTF_Type {
        TKB,
        DIEM,
        HOC_PHI,
        DEFAULT
    }
}
