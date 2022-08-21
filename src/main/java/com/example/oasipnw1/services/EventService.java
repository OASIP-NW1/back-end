package com.example.oasipnw1.services;

import com.example.oasipnw1.dtos.EventDTO;
import com.example.oasipnw1.dtos.EventPageDTO;
import com.example.oasipnw1.dtos.EventUpdateDTO;
import com.example.oasipnw1.dtos.UserUpdateDTO;
import com.example.oasipnw1.entites.Event;
import com.example.oasipnw1.entites.User;
import com.example.oasipnw1.repository.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.time.ZonedDateTime;
import java.util.List;

//
@Service

public class EventService  {

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EventRepository repository;

    public Event save(Event event) {
        ZonedDateTime newEventStartTime = event.getEventStartTime();
        ZonedDateTime newEventEndTime = findEndDate(event.getEventStartTime(), event.getEventDuration());
        List<EventDTO> eventList = getAllEvent();

//        for (int i = 0; i < eventList.size(); i++) {
//            ZonedDateTime eventStartTime = eventList.get(i).getEventStartTime();
//            if(eventStartTime.isEqual(newEventStartTime)){
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time is overlapping");
//            }else {
//                ZonedDateTime eventEndTime = findEndDate(eventList.get(i).getEventStartTime(),
//                        eventList.get(i).getEventDuration());
//                if (newEventStartTime.isBefore(eventStartTime) && newEventEndTime.isAfter(eventStartTime) ||
//                        newEventStartTime.isBefore(eventEndTime) && newEventEndTime.isAfter(eventEndTime) ||
//                        newEventStartTime.isBefore(eventStartTime) && newEventEndTime.isAfter(eventEndTime) ||
//                        newEventStartTime.isAfter(eventStartTime) && newEventEndTime.isBefore(eventEndTime)
//                        || newEventStartTime.equals(eventStartTime)) {
//                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time is overlapping");
//                }
//            }
//        }
        return repository.saveAndFlush(event);
    }

    public ZonedDateTime findEndDate(ZonedDateTime date, Integer duration){
        return date.plusMinutes(duration);
    }

    public List<EventDTO> getAllEvent() {
        List<Event> eventList = repository.findAllByOrderByEventStartTimeDesc();
        return listMapper.mapList(eventList, EventDTO.class, modelMapper);
    }

    @Autowired
    private EventService(EventRepository repository) {
        this.repository = repository;
    }

    public EventDTO getEventById(Integer id) {
        Event event = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Event id " + id +
                        "Does Not Exist !!!"
                ));
        return modelMapper.map(event, EventDTO.class);
    }

    public EventPageDTO getAllEventPage(int page, int pageSize, String sortBy) {
        Sort sort = Sort.by(sortBy);
        return modelMapper.map(repository.findAll(
                        PageRequest.of(page, pageSize, sort.descending())),
                EventPageDTO.class);

    }

    public EventUpdateDTO updateEvent(EventUpdateDTO updateEvent, Integer id) {
        Event event = repository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, id + "does not exist!!!"));

        event.setEventStartTime(updateEvent.getEventStartTime());
        event.setEventNote(updateEvent.getEventNote());
        repository.saveAndFlush(event);
        return updateEvent;

    }
}