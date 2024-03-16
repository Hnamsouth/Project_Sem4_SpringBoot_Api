package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.entities.enums.EPermission;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permission")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EPermission name;

    @ManyToMany(mappedBy = "permission")
    private List<Role> roles;

    public Permission(EPermission permission) {
        this.name = permission;
    }
}
