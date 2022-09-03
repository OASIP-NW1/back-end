package com.example.oasipnw1.controller;

import com.example.oasipnw1.dtos.UserLoginDTO;
import com.example.oasipnw1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequestMapping("/api/login")
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity loginDTO
            (@Valid @RequestBody UserLoginDTO userLogin, HttpServletResponse httpServletResponse, ServletWebRequest request) throws Exception{
        return userService.loginDTO(userLogin,httpServletResponse,request);
    }
}
