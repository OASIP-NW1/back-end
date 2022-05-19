package com.example.oasipkw1.services;

import com.example.oasipkw1.dtos.EventCategoryDTO;
import com.example.oasipkw1.dtos.SimpleEventCategoryDTO;
import com.example.oasipkw1.entites.EventCategory;
import com.example.oasipkw1.repository.EventCategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EventCategoryService {
    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    public List<EventCategoryDTO> getAllEvent() {
        List<EventCategory> eventList = eventCategoryRepository.findAll();
        return listMapper.mapList(eventList, EventCategoryDTO.class, modelMapper);
    }
    public EventCategory updateCategory(SimpleEventCategoryDTO updateCategory, Integer categoryId) {
        EventCategory existingCategory = eventCategoryRepository.findById(categoryId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, categoryId + " Dose not exits!!!"));

        existingCategory.setEventCategoryName(updateCategory.getEventCategoryName());
        existingCategory.setEventCategoryDescription(updateCategory.getEventCategoryDescription());
        existingCategory.setEventDuration(updateCategory.getEventDuration());
        return eventCategoryRepository.saveAndFlush(existingCategory);
    }
}
