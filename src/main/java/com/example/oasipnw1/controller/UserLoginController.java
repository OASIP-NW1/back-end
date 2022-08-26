package com.example.oasipnw1.controller;

import com.example.oasipnw1.dtos.UserCreateDTO;
import com.example.oasipnw1.dtos.UserLoginDTO;
import com.example.oasipnw1.entites.User;
import com.example.oasipnw1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/match")
public class UserLoginController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public UserLoginDTO loginDTO
            (@Valid @RequestBody UserLoginDTO userLogin) {
        return userService.loginDTO(userLogin);
    }

}
