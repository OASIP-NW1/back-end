package com.example.oasipnw1.services;

import com.example.oasipnw1.config.JwtTokenUtil;
import com.example.oasipnw1.dtos.EventDTO;
import com.example.oasipnw1.dtos.EventDetailDTO;
import com.example.oasipnw1.dtos.EventPageDTO;
import com.example.oasipnw1.dtos.EventUpdateDTO;
import com.example.oasipnw1.entites.Event;
import com.example.oasipnw1.entites.EventCategory;
import com.example.oasipnw1.entites.User;
import com.example.oasipnw1.model.HandleException;
import com.example.oasipnw1.repository.EventCategoryRepository;
import com.example.oasipnw1.repository.EventRepository;
import com.example.oasipnw1.repository.UserRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private EmailSerderService serderService;
    @Autowired
    private EventCategoryRepository eventCategoryRepository;

    public EventService(EventCategoryRepository eventCategoryRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
    }

    public Event save(@Valid HttpServletRequest request, @Valid EventDTO eventDTO, MultipartFile multipartFile) throws IOException {
//          file
        Event et = new Event();
        EventCategory ec = new EventCategory();
//        overlap
        LocalDateTime newEventStartTime = eventDTO.getEventStartTime();
        LocalDateTime newEventEndTime = findEndDate(eventDTO.getEventStartTime(), eventDTO.getEventDuration());
        List<EventDTO> eventList = getAllEvent();

        for (int i = 0; i < eventList.size(); i++) {
            LocalDateTime eventStartTime = eventList.get(i).getEventStartTime();
            if (eventStartTime.isEqual(newEventStartTime)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Time is overlapping");
            } else {
                LocalDateTime eventEndTime = findEndDate(eventList.get(i).getEventStartTime(),
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

//            eventCategory
        ec.setId(eventDTO.getEventCategory().getId());
        ec.setEventCategoryName(eventDTO.getEventCategory().getEventCategoryName());
        ec.setEventCategoryDescription(eventDTO.getEventCategory().getEventCategoryDescription());
        ec.setEventDuration(eventDTO.getEventCategory().getEventDuration());

//          event
        et.setBookingName(eventDTO.getBookingName());
        et.setBookingEmail(eventDTO.getBookingEmail());
        et.setEventNote(eventDTO.getEventNote());
        et.setEventStartTime(eventDTO.getEventStartTime());
        et.setEventDuration(eventDTO.getEventDuration());
        et.setEventCategory(ec);
//        et.setFileName(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
//        et.getFileData(multipartFile.getBytes());

        if (multipartFile != null) {
            et.setFileName(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        }

        Event e = modelMapper.map(et, Event.class);
//          check role bookingemail
        if (request.getHeader("Authorization") != null) {
            String getUserEmail = getUserEmail(getRequestAccessToken(request));
            if (request.isUserInRole("student")) {
                if (getUserEmail.equals(eventDTO.getBookingEmail())) {
                    System.out.println("Booking email same as the student's email!");
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking email must be the same as the student's email");
                }
            }
//            send format email
            try {
                LocalDateTime localDateTime = e.getEventStartTime();
                System.out.println("Bookingemail" + " : " + e.getBookingEmail());
                System.out.println("Bookingname" + " : " + e.getBookingName());
                System.out.println("Category" + " : " + e.getEventCategory().getEventCategoryName());
                String newformat = localDateTime.format(DateTimeFormatter.ofPattern("E MMM dd, yyyy HH:mm")
                        .withZone(ZoneId.of("UTC")));
                String header = "You have made a new appointment ." + '\n';
                String body =
                        "Subject : [OASIP]" + " " + e.getEventCategory().getEventCategoryName() + " " + "@" + " "
                                + newformat + " " + "-" + " " + findEndDate(e.getEventStartTime(),
                                e.getEventDuration()).toString().substring(11) + " (ICT)" + '\n' +
                                "Reply-to : " + "noreply@intproj21.sit.kmutt.ac.th" + '\n' +
                                "Booking Name : " + e.getBookingName() + '\n' +
                                "Event Category : " + e.getEventCategory().getEventCategoryName() + '\n' +
                                "When : " + newformat + " " + "-" + " " + findEndDate(e.getEventStartTime(),
                                e.getEventDuration()).toString().substring(11) + " (ICT)" + '\n' +
                                "Event note : " + e.getEventNote();
                serderService.sendSimpleMail(eventDTO.getBookingEmail(), header, body);
                System.out.println("Message Email : " + "Email Sent Succesfully");
            } catch (Exception ex) {
                System.out.println(ex);
                System.out.println("Email Sent Failed");
            }
//          getId send file
            Event saveEvent = repository.saveAndFlush(e);
            sendFile(multipartFile, saveEvent.getId());
        }
        return repository.saveAndFlush(e);
    }

    // file
    public void sendFile(MultipartFile multipartFile, Integer id) {
        try {
            if (multipartFile != null) {
                fileStorageService.storeFile(multipartFile, id);
                System.out.println("store success");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
//            System.out.println(e);
        } catch (MaxUploadSizeExceededException maxUploadSizeExceededException) {
            System.out.println(maxUploadSizeExceededException);
            throw new HandleException(HttpStatus.BAD_REQUEST, "File Upload");
        }
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
        } else if (request.isUserInRole("lecturer")) {
            ArrayList<EventCategory> listCategory = new ArrayList<>();
            List<Event> eventsListByCategoryOwner = repository.findEventCategoryOwnerByEmail(getUserEmail);
            System.out.println(eventsListByCategoryOwner);
            for (Event event : eventsListByCategoryOwner) {
                listCategory.add(event.getEventCategory());
            }
            if (listCategory.contains(events.getEventCategory())) {
                System.out.println("Yes Owner");
                return modelMapper.map(events, EventDetailDTO.class);
            } else {
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


    public EventUpdateDTO updateEvent(EventUpdateDTO updateEvent,
                                      Integer id, MultipartFile multipartFile,
                                      HttpServletRequest request) throws IOException {
        Event event = repository.findById(id).map(events -> {
            events.setEventNote(updateEvent.getEventNote());
            events.setEventStartTime(updateEvent.getEventStartTime());
            events.setFileName(updateEvent.getFileName());
//            events.getFileName(updateEvent.getFileName(multipartFile.getOriginalFilename()));
            return events;
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Can't update event, event id" + id + "doesn't exist."));
        if (request.isUserInRole("student")) {
            System.out.println("Booking email same as the student's email!!!");
            repository.saveAndFlush(event);
        } else {
            System.out.println("Booking email must be the same as student's email!!!");
            throw new AccessDeniedException("");
        }
        //  file
        Path getPath = fileStorageService.getPathFile(id);
        System.out.println("new path: " + getPath);
        File directoryPath = new File(getPath.toString());
        File[] files = directoryPath.listFiles();

        if (multipartFile == null && directoryPath.isDirectory() == false) {
            repository.saveAndFlush(event);
            return updateEvent;
        }
        if (directoryPath.isDirectory() == true) {
            if (multipartFile == null) {
                System.out.println("file is null");
                System.out.println("This is delete file");
                //  check file in folder
                for (File f : files) {
                    if (f.isFile() && f.exists()) {
                        FileUtils.deleteDirectory(new File(getPath.toString()));
                        System.out.println(f.getName());
                        System.out.println("successfully deleted");
                    } else {
                        System.out.println("can't delete a file due to open or error");
                    }
                }
            } else {
                directoryPath.isDirectory();
                System.out.println("directory: " + directoryPath.isDirectory());
                String fileName = null;
                for (File file1 : files) {
                    if (file1.isFile()) {
                        fileName = file1.getName();
                    }
                }
                System.out.println("same file: " + fileName);
                System.out.println("current file: " + multipartFile.getOriginalFilename());
                System.out.println("check same file: " + multipartFile.getOriginalFilename().equals(fileName));
                if (multipartFile.getOriginalFilename().isEmpty()) {
                    System.out.println("Equal file name");
                } else {
                    System.out.println("No equal file name");
                    if (Files.exists(Path.of(getPath.toString()))) {
                        FileUtils.cleanDirectory(new File(getPath.toString()));
                        System.out.println("have file");
                        fileStorageService.storeFile(multipartFile, id);
                        event.setFileName(multipartFile.getOriginalFilename());
                        updateEvent.setFileName(multipartFile.getOriginalFilename());
                    }
                    fileStorageService.storeFile(multipartFile, id);
                    event.setFileName(multipartFile.getOriginalFilename());
                    updateEvent.setFileName(multipartFile.getOriginalFilename());
                }
                System.out.println(event.getFileName());
            }
        } else {
            Files.createDirectories(directoryPath.toPath());
            fileStorageService.storeFile(multipartFile, id);
            event.setFileName(multipartFile.getOriginalFilename());
            updateEvent.setFileName(multipartFile.getOriginalFilename());
            System.out.println(event.getFileName());
        }
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