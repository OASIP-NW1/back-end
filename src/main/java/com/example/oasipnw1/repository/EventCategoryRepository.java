package com.example.oasipnw1.repository;

import com.example.oasipnw1.entites.Event;
import com.example.oasipnw1.entites.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Integer> {
//    List<Event> findByEventCategoryId(Integer eventCategoryId);
}

