package com.example.project_sem4_springboot_api.entities;

import com.example.project_sem4_springboot_api.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    private String realPassword;
    private String token;
    @Column(name = "create_at")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "status_id")
    @JsonBackReference
    private Status status;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference
    private List<UserDetail> userDetail;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<UserNotification> notifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private List<UserDeviceToken> userDeviceTokens;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonBackReference
    private Teacher teacher ;

    @JsonBackReference
    @ManyToMany(mappedBy = "parents",fetch = FetchType.EAGER)
    private List<Student> students;

    @JsonIgnore
    public UserDto getDto(){
        return UserDto.builder()
            .id(this.id).username(this.username).password(this.realPassword).roles(this.roles).status(this.status)
            .userDetail(userDetail.get(0).getDto(false)).build();
    }
}