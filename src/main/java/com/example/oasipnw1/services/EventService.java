package com.example.oasipnw1.services;

import com.example.oasipnw1.config.JwtTokenUtil;
import com.example.oasipnw1.dtos.EventDTO;
import com.example.oasipnw1.dtos.EventPageDTO;
import com.example.oasipnw1.dtos.EventUpdateDTO;
import com.example.oasipnw1.entites.Event;
import com.example.oasipnw1.repository.EventRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Properties;

//
@Service

public class EventService  {

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

    public Event save(@Valid HttpServletRequest request, Event event) {
        ZonedDateTime newEventStartTime = event.getEventStartTime();
        ZonedDateTime newEventEndTime = findEndDate(event.getEventStartTime(), event.getEventDuration());
        List<EventDTO> eventList = getAllEvent();

        for (int i = 0; i < eventList.size(); i++) {
            ZonedDateTime eventStartTime = eventList.get(i).getEventStartTime();
            if(eventStartTime.isEqual(newEventStartTime)){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time is overlapping");
            }else {
                ZonedDateTime eventEndTime = findEndDate(eventList.get(i).getEventStartTime(),
                        eventList.get(i).getEventDuration());
                if (newEventStartTime.isBefore(eventStartTime) && newEventEndTime.isAfter(eventStartTime) ||
                        newEventStartTime.isBefore(eventEndTime) && newEventEndTime.isAfter(eventEndTime) ||
                        newEventStartTime.isBefore(eventStartTime) && newEventEndTime.isAfter(eventEndTime) ||
                        newEventStartTime.isAfter(eventStartTime) && newEventEndTime.isBefore(eventEndTime)
                        || newEventStartTime.equals(eventStartTime)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time is overlapping");
                }
            }
        }
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

    public List<EventDTO> getAll(HttpServletRequest request){
        List<Event> eventsList = repository.findAll(Sort.by(Sort.Direction.DESC,"eventStartTime"));
        String getUserEmail = getUserEmail(getRequestAccessToken(request));
        UserDetails userDetails = jwtuserDetailsService.loadUserByUsername(getUserEmail);
        if(userDetails != null && (request.isUserInRole("ROLE_student"))){
            List<Event> eventsListByEmail = repository.findByBookingEmail(getUserEmail);
            return listMapper.mapList(eventsListByEmail, EventDTO.class,modelMapper);

        }
        else if(userDetails != null && (request.isUserInRole("ROLE_lecturer"))){
//            List<Events> eventsListByEmail = repository.findByBookingEmail(getUserEmail);
            List<Event> eventListByCategoryOwner = repository.findEventCategoryOwnerByEmail(getUserEmail);

            return listMapper.mapList(eventListByCategoryOwner , EventDTO.class,modelMapper);

        }
        return listMapper.mapList(eventsList, EventDTO.class,modelMapper);
    }

    public String getRequestAccessToken(HttpServletRequest request){
        return request.getHeader("Authorization").substring(7);
    }

    public String getUserEmail(String requestAccessToken){
        return jwtTokenUtil.getUsernameFromToken(requestAccessToken);
    }

//    private void sendmail(Event event) throws AddressException, MessagingException, IOException {
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.port", "587");
//
//        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("wppoyo@gmail.com", "jgxnxaniytaytjxa");
//            }
//        });
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mm a").withZone(ZoneId.systemDefault());
//
//        Message msg = new MimeMessage(session);
//        msg.setFrom(new InternetAddress("wppoyo@gmail.com", false));
//
//        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(event.getBookingEmail()));
//        msg.setSubject("Your booking is complete.");
//        msg.setContent("Your booking name : " + event.getBookingName() +
//                        "<br> Event category : " + event.getEventCategory() +
//
//                        "<br><br>Start date and time : " + formatter.format(event.getEventStartTime()) +
//                        "<br>Event duration : " + event.getEventDuration() +
//
//                        "<br><br>Event note : " + event.getEventNote()
//                , "text/html");
//        msg.setSentDate(new Date());
//        Transport.send(msg);
//    }
}