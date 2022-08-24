package com.example.oasipnw1.controller;


import com.example.oasipnw1.dtos.UserCreateDTO;
import com.example.oasipnw1.dtos.UserDTO;
import com.example.oasipnw1.dtos.UserUpdateDTO;
import com.example.oasipnw1.entites.User;
import com.example.oasipnw1.repository.UserRepository;
import com.example.oasipnw1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    public UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public List<UserDTO> getAll(){
        return userService.getAllUser();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Integer id){
        return userService.getUserById(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody UserCreateDTO newUser) {
        return userService.save(newUser);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserUpdateDTO update(@Valid @RequestBody UserUpdateDTO updateUser,
                                @PathVariable Integer id) {
        return userService.updateUser(updateUser,id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id ) {
        userRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        id + " does not exist !!!"));
        userRepository.deleteById(id);
    }
}

