package com.example.oasipnw1.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class UserLoginDTO {

    private String name;

    private String password;

    @Email(message = "invalid email format")
    private String email;
}
