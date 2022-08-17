package com.example.oasipnw1.repository;

import com.example.oasipnw1.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
