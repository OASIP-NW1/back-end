package com.example.oasipnw1.services;

import com.example.oasipnw1.dtos.UserLoginDTO;
import com.example.oasipnw1.model.Response;
import com.example.oasipnw1.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MatchService {
    private final UserRepository repository;
    private final ListMapper listMapper;
    private final ModelMapper modelMapper;
    private final Argon2PasswordEncoder argon2PasswordEncoder;

    public MatchService(UserRepository repository, ListMapper listMapper, ModelMapper modelMapper, Argon2PasswordEncoder argon2PasswordEncoder) {
        this.repository = repository;
        this.listMapper = listMapper;
        this.modelMapper = modelMapper;
        this.argon2PasswordEncoder = argon2PasswordEncoder;
    }

    public ResponseEntity matchCheck(UserLoginDTO userLoginDTO) {
        if (repository.existsByEmail(userLoginDTO.getEmail())) {
            if (argon2PasswordEncoder.matches(String.valueOf(userLoginDTO.getPassword()), repository.findByEmail(userLoginDTO.getEmail()).getPassword())) {
                return ResponseEntity.ok("Password match!");
            }
            return Response.response(HttpStatus.UNAUTHORIZED, "Password doesn't match");
        }
        return Response.response(HttpStatus.NOT_FOUND, "User not found username : " + userLoginDTO.getEmail());
    }
}