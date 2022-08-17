package com.example.oasipnw1.repository;

import com.example.oasipnw1.entites.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventPageRepository extends JpaRepository<Event, Integer> {
}
