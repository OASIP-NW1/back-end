package com.example.oasipkw1.services;

import com.example.oasipkw1.dtos.EventCategoryDTO;
import com.example.oasipkw1.entites.EventCategory;
import com.example.oasipkw1.repository.EventCategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
}
