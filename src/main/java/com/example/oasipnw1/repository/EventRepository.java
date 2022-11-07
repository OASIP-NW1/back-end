package com.example.oasipnw1.repository;

import com.example.oasipnw1.entites.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByOrderByEventStartTimeDesc();
    List<Event> findByBookingEmail(String email);
//    List<Event> findByEventCategoryID(Integer eventCategoryID, Sort sort);
    @Query(value = "SELECT e1 FROM Event e1 JOIN EventCategoryOwner e2 ON e1.eventCategory.id = e2.eventCategoryID.id " +
            "JOIN User u ON u.id = e2.userID.id WHERE u.email = :email")
    List<Event> findEventCategoryOwnerByEmail(@Param("email") String email);
}
