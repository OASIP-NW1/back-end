package com.example.oasipkw1.controller;

import com.example.oasipkw1.dtos.EventCategoryDTO;
import com.example.oasipkw1.dtos.SimpleEventCategoryDTO;
import com.example.oasipkw1.entites.EventCategory;
import com.example.oasipkw1.services.EventCategoryService;
import com.example.oasipkw1.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/eventCategory")
public class EventCategoryController {
    @Autowired
    EventCategoryService eventCategoryService;

    @GetMapping("")
    public List<EventCategoryDTO> getAll(){
        return eventCategoryService.getAllEvent();
    }

    @PutMapping("/{id}")
    public EventCategory updateCategory(@RequestBody SimpleEventCategoryDTO updateCategory, @PathVariable Integer id) {
        return eventCategoryService.updateCategory(updateCategory, id);
    }
}