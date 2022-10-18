package com.example.oasipnw1.services;

import com.example.oasipnw1.config.JwtTokenUtil;
import com.example.oasipnw1.dtos.EventDTO;
import com.example.oasipnw1.dtos.EventDetailDTO;
import com.example.oasipnw1.dtos.EventPageDTO;
import com.example.oasipnw1.dtos.EventUpdateDTO;
import com.example.oasipnw1.entites.Event;
import com.example.oasipnw1.entites.EventCategory;
import com.example.oasipnw1.entites.User;
import com.example.oasipnw1.repository.EventCategoryOwnerRepository;
import com.example.oasipnw1.repository.EventRepository;
import com.example.oasipnw1.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service

public class EventService {

    @Autowired
    private ListMapper listMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EventRepository repository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtuserDetailsService;
//
//    @Autowired
//    private EmailSerderService emailSerderService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventCategoryOwnerRepository eventCategoryOwnerRepository;
    public Event save(@Valid HttpServletRequest request, Event event) {
        Event e = modelMapper.map(event, Event.class);
        String getUserEmail = getUserEmail(getRequestAccessToken(request));
        if (request.isUserInRole("student")) {
            if (getUserEmail.equals(event.getBookingEmail())) {
                System.out.println("Booking email same as the student's email!");
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking email must be the same as the student's email");
            }
        }
        try {
            LocalDateTime localDateTime = e.getEventStartTime();
            System.out.println(e.getEventCategory().getEventCategoryName());
            String newformat = localDateTime.format(DateTimeFormatter.ofPattern("E MMM dd, yyyy HH:mm").withZone(ZoneId.of("UTC")));
            String header = "You have made a new appointment ." + '\n';
            String body =
                            "Subject : [OASIP]" + " " + e.getEventCategory().getEventCategoryName() + " " + "@" + " " + newformat + " " + "-" + " " + findEndDate(e.getEventStartTime(),e.getEventDuration()).toString().substring(11) + " (ICT)" + '\n' +
                            "Reply-to : " + "noreply@intproj21.sit.kmutt.ac.th" + '\n' +
                            "Booking Name : " + e.getBookingName() + '\n' +
                            "Event Category : " + e.getEventCategory().getEventCategoryName() + '\n' +
                            "When : " + newformat + " " + "-" + " " + findEndDate(e.getEventStartTime(),e.getEventDuration()).toString().substring(11) + " (ICT)" + '\n' +
//                          "When : " + " " + e.getEventStartTime().toString().replace("T" , " ")+ " " + "-" + " " + findEndDate(e.getEventStartTime(),e.getEventDuration()).toString().substring(11) + '\n' +
//                          "Event duration : " + e.getEventDuration() + "Minutes" + '\n' +
                            "Event note : "  + e.getEventNote();
//            emailSerderService.sendSimpleMail(e.getBookingEmail(), header, body);
            System.out.println("email sent succesfully");
        } catch (Exception ex) {
            System.out.println(ex);
            System.out.println("email sent failed");
        }

        return repository.saveAndFlush(event);
    }

    public EventDetailDTO getEventById(Integer id, HttpServletRequest request) {
        Event events = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't get event, event id " + id +
                        " doesn't exist."
                ));
        String getUserEmail = getUserEmail(getRequestAccessToken(request));
        if (request.isUserInRole("student")) {
            if (getUserEmail.equals(events.getBookingEmail())) {
                System.out.println("Booking email same as the student's email!");
                return modelMapper.map(events, EventDetailDTO.class);
            } else {
                System.out.println("Booking email must be the same as the student's email!");
                throw new AccessDeniedException("");
            }
        }else if(request.isUserInRole("lecturer")){
            ArrayList<EventCategory> listCategory = new ArrayList<>();
            List<Event> eventsListByCategoryOwner = repository.findEventCategoryOwnerByEmail(getUserEmail);
            System.out.println(eventsListByCategoryOwner);
            for(Event event : eventsListByCategoryOwner){
                listCategory.add(event.getEventCategory());
            }
            if(listCategory.contains(events.getEventCategory())){
                System.out.println("Yes Owner");
                return modelMapper.map(events,EventDetailDTO.class);
            }else{
                System.out.println("No owner");
                throw new AccessDeniedException("");
            }
        }
        return modelMapper.map(events, EventDetailDTO.class);
    }


    public LocalDateTime findEndDate(LocalDateTime date, Integer duration) {
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

    public User getUserFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        String userEmail = jwtTokenUtil.getUsernameFromToken(token);
        return  userRepository.findByEmail(userEmail);
    }

    public List<EventDTO> getAll(HttpServletRequest request) {
        List<Event> eventsList = repository.findAll(Sort.by(Sort.Direction.DESC, "eventStartTime"));
        String getUserEmail = getUserEmail(getRequestAccessToken(request));
        UserDetails userDetails = jwtuserDetailsService.loadUserByUsername(getUserEmail);
        if (userDetails != null && (request.isUserInRole("ROLE_student"))) {
            List<Event> eventsListByEmail = repository.findByBookingEmail(getUserEmail);
            return listMapper.mapList(eventsListByEmail, EventDTO.class, modelMapper);
        } else if (userDetails != null && (request.isUserInRole("ROLE_lecturer"))) {
//            List<Events> eventsListByEmail = repository.findByBookingEmail(getUserEmail);
            List<Event> eventListByCategoryOwner = repository.findEventCategoryOwnerByEmail(getUserEmail);

            return listMapper.mapList(eventListByCategoryOwner , EventDTO.class,modelMapper);
        }
        return listMapper.mapList(eventsList, EventDTO.class, modelMapper);
    }

    public String getRequestAccessToken(HttpServletRequest request) {
        return request.getHeader("Authorization").substring(7);
    }

    public String getUserEmail(String requestAccessToken) {
        return jwtTokenUtil.getUsernameFromToken(requestAccessToken);
    }

}