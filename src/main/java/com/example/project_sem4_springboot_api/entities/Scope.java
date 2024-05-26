package com.example.project_sem4_springboot_api.entities;


import com.example.project_sem4_springboot_api.entities.enums.EScope;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "scope")
public class Scope {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private EScope code;

    public Scope(EScope eScope) {
        this.name = eScope.getScope();
        this.code = eScope;
    }
}
