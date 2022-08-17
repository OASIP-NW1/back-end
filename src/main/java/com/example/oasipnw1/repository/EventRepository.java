package com.example.oasipnw1.repository;

import com.example.oasipnw1.entites.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByOrderByEventStartTimeDesc();
}
