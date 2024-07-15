package com.example.project_sem4_springboot_api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_device_token")
public class UserDeviceToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String deviceToken;
    private String os;
    private String deviceName;
    private LocalDate createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonManagedReference
    private User user;

    @JsonIgnore
    public Map<String,Object> toRes(){
        return new LinkedHashMap<>(
                Map.of("id",id,
                        "deviceToken",deviceToken,
                        "os",os,
                        "createdAt",createdAt,
                        "userId",user.getId()
                )
        );
    }

}
