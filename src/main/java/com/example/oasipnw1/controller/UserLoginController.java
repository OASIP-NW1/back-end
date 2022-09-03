package com.example.oasipnw1.controller;

import com.example.oasipnw1.dtos.UserCreateDTO;
import com.example.oasipnw1.dtos.UserLoginDTO;
import com.example.oasipnw1.entites.User;
import com.example.oasipnw1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/login")
//@RequestMapping("/api/match")
public class UserLoginController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity loginDTO
            (@Valid @RequestBody UserLoginDTO userLogin, HttpServletResponse httpServletResponse, ServletWebRequest request) throws Exception{
        return userService.loginDTO(userLogin,httpServletResponse,request);
    }

}
