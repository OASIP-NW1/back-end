package com.example.oasipnw1.dtos;

import com.example.oasipnw1.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    private Integer id;
    private String name;
    private String email;
    private Role role;

//    public UserUpdateDTO(int id , String name , String email , Role role){
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.role = role;
//    }

}
