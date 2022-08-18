package com.example.oasipnw1.dtos;

import com.example.oasipnw1.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Constraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserDTO {
    private Integer id;

    @NotBlank(message = "name can't be blank")
    @Size(max = 100, message = "booking name must be between 0-100 characters")
    private String name;

    @NotBlank(message = "booking-email can't be blank")
    @Size(max = 50 , message = "booking-email must be between 0-50 characters")
    @Email(message = "invalid email format")
    private String email;

    private Role role = Role.student;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime createdOn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime updatedOn;
}
