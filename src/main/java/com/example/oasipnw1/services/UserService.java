package com.example.oasipnw1.services;

import com.example.oasipnw1.dtos.UserCreateDTO;
import com.example.oasipnw1.dtos.UserDTO;
import com.example.oasipnw1.dtos.UserUpdateDTO;
import com.example.oasipnw1.entites.User;
import com.example.oasipnw1.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;


    public List<UserDTO> getAllUser() {
        List<User> userList = userRepository.findAll();
        return listMapper.mapList(userList, UserDTO.class, modelMapper);
    }

    public UserDTO getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User id " + id +
                        "Does Not Exist !!!"
                ));
        return modelMapper.map(user, UserDTO.class);
    }

    public User save(UserCreateDTO newUser) {
        newUser.setName(newUser.getName().trim());
        newUser.setEmail(newUser.getEmail().trim());
        User user = modelMapper.map(newUser, User.class);
        return userRepository.saveAndFlush(user);
    }

    public UserUpdateDTO updateUser(UserUpdateDTO updateUser, Integer id){
        User user = userRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, id + "does not exist!!!"));
        user.setName(updateUser.getName().trim());
        user.setEmail(updateUser.getEmail().trim());
//        user.setRole(updateUser.getRole());
//        System.out.println(user.getRole().equals(Roles.student));
//        System.out.println(!(user.getRole().equals(Roles.student)));
//        System.out.println(updateUser.getRole());
//        System.out.println(user.getRole());
        if(!(user.getRole().equals(updateUser.getRole()))){
            user.setRole(updateUser.getRole());
        }
        userRepository.saveAndFlush(user);
        return updateUser;
    }
}
