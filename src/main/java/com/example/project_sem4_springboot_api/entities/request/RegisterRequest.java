package com.example.project_sem4_springboot_api.entities.request;

import com.example.project_sem4_springboot_api.entities.Role;
import com.example.project_sem4_springboot_api.entities.Status;
import com.example.project_sem4_springboot_api.entities.User;
import com.example.project_sem4_springboot_api.entities.UserDetail;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest  implements Serializable {

    @NotBlank(message = "Username is required")
    @Size(min = 6, max = 50, message = "Username must be between 6 and 50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 255, message = "Password must be between 6 and 255 characters")
    private String password;

    @NotEmpty(message = "Role is required")
    private Set<@Min(1) Long> role;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private  String first_name;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private  String last_name;

    @NotBlank(message = "Address is required")
    @Size(min = 2, max = 255, message = "Address must be between 2 and 255 characters")
    private  String address;

    @NotBlank
    @Size(min = 10, max = 13, message = "Phone must be 10 or 13 characters")
    private  String phone;

    @NotBlank(message = "Email is required")
    @Size(min = 7, max = 100, message = "Email must be between 6 and 50 characters")
    @Email(message = "Email is invalid")
    private  String email;

    @NotNull(message = "Gender is required")
    private  boolean gender;

    private  Date birthday;
    @Size(min = 9, max = 12, message = "Citizen ID must be between 9 and 12 characters")
    private  String citizen_id;

    @Size(min = 9, max = 12, message = "Passport must be between 9 and 12 characters")
    private  String nation;

    private  String avatar;

    @JsonIgnore
    public  UserDetail toUserDetail(User user){
        return UserDetail.builder().address(address).phone(phone).birthday(birthday)
            .avatar(avatar).gender(this.isGender()).firstname(first_name).lastname(last_name)
            .email(email).nation(nation).citizen_id(citizen_id).user(user).build();
    }
    @JsonIgnore
    public  User toUser(Set<Role> roles, String pwEncode, Status status){
        return User.builder().username(username).password(pwEncode).realPassword(password)
            .roles(roles)
            .status(status)
            .createdAt(new java.sql.Date(System.currentTimeMillis())).build();
    }
}
