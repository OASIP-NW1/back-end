package com.example.oasipnw1.dtos;

import com.example.oasipnw1.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserCreateDTO {

    private Integer id;
    @NotBlank(message = "name can't be blank")
    @Size(max = 100, message = "booking name must be between 0-100 characters")
    private String name;

    @NotBlank(message = "booking-email can't be blank")
    @Size(max = 50 , message = "booking-email must be between 0-50 characters")
    @Email(message = "invalid email format")
    private String email;

    @Enumerated()
    private Role role = Role.student;

}
