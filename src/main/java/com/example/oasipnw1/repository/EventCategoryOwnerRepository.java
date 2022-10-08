package com.example.oasipnw1.repository;

import com.example.oasipnw1.entites.EventCategoryOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventCategoryOwnerRepository extends JpaRepository<EventCategoryOwner, Integer> {
//    @Query("select a.eventCategoryID.id from EventCategoryOwner a where a.userID.id = :id ")
//    List<Integer> findAllByUserId(@Param("id") int id);
}
