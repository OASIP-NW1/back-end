package com.example.oasipkw1.controller;


import com.example.oasipkw1.dtos.EventDTO;
import com.example.oasipkw1.dtos.UserDTO;
import com.example.oasipkw1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("")
    @ResponseStatus(code = HttpStatus.OK)
    public List<UserDTO> getAllUser(){
        return userService.getAll();
    }
}
