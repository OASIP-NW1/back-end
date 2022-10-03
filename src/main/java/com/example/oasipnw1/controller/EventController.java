package com.example.oasipnw1.controller;

import com.example.oasipnw1.dtos.*;
import com.example.oasipnw1.entites.Event;
import com.example.oasipnw1.entites.EventCategory;
import com.example.oasipnw1.repository.EventCategoryRepository;
import com.example.oasipnw1.repository.EventRepository;
import com.example.oasipnw1.services.EmailSerderService;
import com.example.oasipnw1.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.event.MailEvent;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private EmailSerderService serderService;
//    public EmailSerderService(EmailSerderService serderService , EventCategoryRepository eventCategoryRepository){
//        this.serderService = serderService;
//        this.eventRepository = eventCategoryRepository;
//    }
    @GetMapping("")
    public List<EventDTO> getAllSubject(HttpServletRequest httpServletRequest){
        return eventService.getAll(httpServletRequest);
    }

    @GetMapping("/{id}")
    public EventDTO getEventById(@PathVariable Integer id){
        return eventService.getEventById(id);
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
    public void Event (@Valid HttpServletRequest request , @Valid @RequestBody Event event) {
//        ZonedDateTime time = event.getEventStartTime();
//        String formattedDate = time.format(DateTimeFormatter.ofPattern("dd-MMM-yy-hh-mm"));
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy-hh-mm");
//        ZonedDateTime time = event.getEventStartTime();
//        String formattedDate = time.format(DateTimeFormatter.ofPattern("dd-MMM-yy-hh-mm"));
//        String header = "You have made a new appointment ." + '\n' ;
//        String body = "Your booking name : " + event.getBookingName() + '\n' +
//                "Event category : " + event.getEventCategory().getEventCategoryName() + '\n' +
//                "Start date and time : " + zonedDateTime.format(event.getEventStartTime())  + '\n' +
//                "Event duration : " + event.getEventDuration() + "Minutes" + '\n' +
//                "Event note : " + event.getEventNote();
//        serderService.sendNotification(event.getBookingEmail(),header , body  );
//        eventService.save (request,event);
        ZonedDateTime time = event.getEventStartTime();
//        String formattedDate = time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String header = "You have made a new appointment.";
        String body = "Your appointment has been registered successfully. \n \n" +
                "Details  \n" + "Name : " + event.getBookingName() + "\n" +"Clinic : " + eventCategory.getEventCategoryName() +
                "\n" + "Date : " + formatter.format(event.getEventStartTime()) + "Time"+ "\n" + "Note : " + event.getEventNote();
        serderService.sendNotification(event.getBookingEmail() , header , body);
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