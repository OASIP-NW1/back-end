package com.example.oasipkw1.services;

import com.example.oasipkw1.dtos.EventDTO;
import com.example.oasipkw1.dtos.EventPageDTO;
import com.example.oasipkw1.entites.Event;
import com.example.oasipkw1.entites.EventCategory;
import com.example.oasipkw1.repository.EventCategoryRepository;
import com.example.oasipkw1.repository.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
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
//        Date newEventStartTime = Date.from(event.getEventStartTime());
//        Date newEventEndTime = findEndDate(Date.from(event.getEventStartTime()), event.getEventDuration());
//        List<EventDTO> eventList = getAllEvent();
//
//        for (int i = 0; i < eventList.size(); i++) {
//            Date eventStartTime = Date.from(eventList.get(i).getEventStartTime());
//            Date eventEndTime = findEndDate(Date.from(eventList.get(i).getEventStartTime()),eventList.get(i).getEventDuration());
//            if (newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventStartTime) ||
//                    newEventStartTime.before(eventEndTime) && newEventEndTime.after(eventEndTime) ||
//                    newEventStartTime.before(eventStartTime) && newEventEndTime.after(eventEndTime) ||
//                    newEventStartTime.after(eventStartTime) && newEventEndTime.before(eventEndTime)
//                    || newEventStartTime.equals(eventStartTime)) {
//
//                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time is overlapping");
//            }
//        }
        return repository.saveAndFlush(event);
    }


//    public Date findEndDate(Date date, Integer duration){
//        return new Date(date.getTime()+(duration*60000));
//    }
//
//
//    public ZonedDateTime findEndDate(ZonedDateTime date, Integer duration){
//        return date.plusMinutes(duration);
//    }

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
}