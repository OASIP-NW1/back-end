package com.example.oasipnw1.controller;

import com.example.oasipnw1.dtos.UserLoginDTO;
import com.example.oasipnw1.services.MatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/match")
public class MatchController {
    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity userLoginDTO(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        return matchService.matchCheck(userLoginDTO);
    }
}
