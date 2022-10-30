package com.example.oasipnw1.controller;

import com.example.oasipnw1.dtos.*;
import com.example.oasipnw1.entites.Event;
import com.example.oasipnw1.entites.EventCategory;
import com.example.oasipnw1.repository.EventRepository;
//import com.example.oasipnw1.services.EmailSerderService;
import com.example.oasipnw1.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    @Autowired
    EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventCategory eventCategory;

    @GetMapping("")
    public List<EventDTO> getAllSubject(HttpServletRequest httpServletRequest){
        return eventService.getAll(httpServletRequest);
    }

    //    @GetMapping("/{id}")
//    public EventDTO getEventById(@PathVariable Integer id){
//        return eventService.getEventById(id);
//    }
    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EventDetailDTO getEventById(@PathVariable Integer id, HttpServletRequest request) {
        return eventService.getEventById(id, request);
    }

    @GetMapping("/page")
    public EventPageDTO getAllEventPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int pageSize,
            @RequestParam(defaultValue = "eventStartTime") String sortBy) {
        return eventService.getAllEventPage(page,pageSize,sortBy);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    @PreAuthorize("!isAuthenticated() or hasAnyRole(\"admin\",\"student\")")
//    public void Event (@Valid HttpServletRequest request , @Valid @RequestBody Event event )
    public void Event (@Valid HttpServletRequest request , @Valid @RequestPart Event event ,@RequestPart(value = "file" , required = false) MultipartFile multipartFile) {
        eventService.save(request,event,multipartFile);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id ) {
        eventRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        id + " does not exist !!!"));
        eventRepository.deleteById(id);
    }

//    @PutMapping("/{id}")
//    @ResponseStatus(code = HttpStatus.OK)
//    public void update(@Valid @RequestBody Event updateEvent,
//                       @PathVariable Integer id) {
//        Event event = eventRepository.findById(id)
//                .map(o -> mapEvent(o, updateEvent))
//                .orElseGet(() -> {
//                    updateEvent.setId(id);
//                    return updateEvent;
//                });
//        eventService.save (event);
//    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EventUpdateDTO updateEvent(@Valid @RequestBody EventUpdateDTO updateEvent,
                                      @PathVariable Integer id) {
        return eventService.updateEvent(updateEvent,id);

    }

//    private Event mapEvent(Event existingEvent , Event updateEvent){
//        existingEvent.setEventStartTime(updateEvent.getEventStartTime());
//        existingEvent.setEventNote(updateEvent.getEventNote());
//        return existingEvent;
//    }
}