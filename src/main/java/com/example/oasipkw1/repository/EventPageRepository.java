package com.example.oasipkw1.repository;

import com.example.oasipkw1.entites.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventPageRepository extends JpaRepository<Event, Integer> {
}
