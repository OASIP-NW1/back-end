package com.example.oasipkw1.repository;

import com.example.oasipkw1.dtos.UserDTO;
import com.example.oasipkw1.entites.Event;
import com.example.oasipkw1.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
