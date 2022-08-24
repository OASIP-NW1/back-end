package com.example.oasipnw1.repository;

import com.example.oasipnw1.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
   public User findByEmail(String Email);
   public boolean existsByEmail(String Email);
//    List<User> existsUserByPassword();
}
