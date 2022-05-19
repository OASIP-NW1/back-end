package com.example.oasipkw1.repository;

import com.example.oasipkw1.entites.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByOrderByEventStartTimeDesc();
}
