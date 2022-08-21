package com.example.oasipnw1.services;

import com.example.oasipnw1.Role;
import com.example.oasipnw1.dtos.UserCreateDTO;
import com.example.oasipnw1.dtos.UserDTO;
import com.example.oasipnw1.dtos.UserUpdateDTO;
import com.example.oasipnw1.entites.User;
import com.example.oasipnw1.repository.UserRepository;
import net.minidev.json.JSONUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        List<User> userList = userRepository.findAll((Sort.by("name").ascending()));
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
        User addUserList = modelMapper.map(newUser, User.class);
        addUserList.setName(newUser.getName().trim());
        addUserList.setEmail(newUser.getEmail().trim());

        //  Check Validtion Unique Create (name email role)
        if(checkUniqueCreate(newUser)){
            addUserList.setRole(newUser.getRole() == null ? Role.student : newUser.getRole());
        }
        return userRepository.saveAndFlush(addUserList);
    }

    public UserUpdateDTO updateUser(UserUpdateDTO updateUser, Integer id){
        User user = userRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, id + "does not exist!!!"));
//        user.setName(updateUser.getName().trim());
//        user.setEmail(updateUser.getEmail().trim());

//       Check Update Role
        if(updateUser.getRole()==null){
            updateUser.setRole(user.getRole());
        }else if(!(user.getRole().equals(updateUser.getRole()))){
            user.setRole(updateUser.getRole());
        }
//        userRepository.saveAndFlush(user);
//        return updateUser;

//        Check Validtion Unique Update (name email role)
        if(checkUniqueUpdate(updateUser , id)){
            user.setName(updateUser.getName().trim());
            user.setEmail(updateUser.getEmail().trim());
            user.setRole((updateUser.getRole() == null) ? user.getRole() : updateUser.getRole());
            userRepository.saveAndFlush(user);
        }
        return updateUser;
    }

    public boolean checkUniqueUpdate (UserUpdateDTO user ,Integer id){
        List<User> allUser = userRepository.findAll();
        for(User users : allUser){
            if(!(users.getId() == id)){
                if(users.getName().trim().equals(user.getName().trim())){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "This name is already used");
                }else if (users.getEmail().trim().equals(user.getEmail().trim())){
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "This email is already used");
                }
            }
        }
        return true;
    }
    public boolean checkUniqueCreate (UserCreateDTO user){
        List<User> allUser = userRepository.findAll();
        for(User users : allUser){
            if(users.getName().trim().equals(user.getName().trim())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "This name is already used");
            }else if (users.getEmail().trim().equals(user.getEmail().trim())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST , "This email is already used");
            }
        }
        return true;
    }
}
