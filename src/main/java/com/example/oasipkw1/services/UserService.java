package com.example.oasipkw1.services;

import com.example.oasipkw1.dtos.EventDTO;
import com.example.oasipkw1.dtos.UserDTO;
import com.example.oasipkw1.entites.Event;
import com.example.oasipkw1.entites.User;
import com.example.oasipkw1.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ListMapper listMapper;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository , ListMapper listMapper , ModelMapper modelMapper){
        this.userRepository = userRepository;
        this.listMapper = listMapper;
        this.modelMapper = modelMapper;
    }

    public List<UserDTO> getAll() {
        List<User> userList = userRepository.findAll();
        return listMapper.mapList(userList, UserDTO.class, modelMapper);
    }
}
