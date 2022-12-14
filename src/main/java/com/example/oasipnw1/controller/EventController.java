package com.example.oasipnw1.controller;

import com.example.oasipnw1.dtos.*;
import com.example.oasipnw1.entites.Event;
import com.example.oasipnw1.entites.EventCategory;
import com.example.oasipnw1.repository.EventRepository;
//import com.example.oasipnw1.services.EmailSerderService;
import com.example.oasipnw1.services.EmailSerderService;
import com.example.oasipnw1.services.EventService;
import com.example.oasipnw1.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Autowired
    private FileStorageService fileStorageService;

    //    @PreAuthorize("hasAnyAuthority('Admin','Student','Lecturer')")
    @GetMapping("")
    public List<EventDTO> getAllSubject(HttpServletRequest httpServletRequest){
        return eventService.getAll(httpServletRequest);
    }

    //    @PreAuthorize("hasAnyAuthority('Admin','Student','Lecturer')")
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
//    @PreAuthorize("!isAuthenticated() or hasAnyRole(\"Admin\",\"Student\")")
//    public void Event (@Valid HttpServletRequest request , @Valid @RequestBody Event event )
    public void EventDTO (@Valid HttpServletRequest request ,
                          @Valid @RequestPart EventDTO eventDTO ,
                          @RequestPart(value = "file" , required = false)
                                      MultipartFile multipartFile) throws IOException {
        eventService.save(request,eventDTO,multipartFile);
    }


    //    @PreAuthorize("hasAnyAuthority('Admin','Student')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id ,@Valid HttpServletRequest request) {
        eventRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        id + " does not exist !!!"));
        fileStorageService.Deletefile(id);
        eventRepository.deleteById(id);
    }
    //    @PreAuthorize("hasAnyAuthority('Admin','Student')")
    @DeleteMapping("/file/{id}")
    public void delete(@PathVariable Integer id) throws IOException {
        fileStorageService.Deletefile(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public EventUpdateDTO updateEvent(@Valid @RequestPart EventUpdateDTO updateEvent,
                                      @PathVariable Integer id,
                                      @RequestPart(value = "file" , required = false) MultipartFile multipartFile,
                                      HttpServletRequest request) throws IOException {
        return eventService.updateEvent(updateEvent,id,multipartFile,request);

    }
}