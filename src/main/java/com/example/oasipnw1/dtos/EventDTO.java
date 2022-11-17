package com.example.oasipnw1.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.constraints.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class EventDTO {
    private Integer id;

    @NotBlank(message = "Booking Name is not empty")
    @Size(max = 100,message = "Booking Name size must between 0 and 100 character")
    private String bookingName;

    @Email(message = "Email must be a well-formed email address" ,
            regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @NotBlank(message = "Email cannot be empty")
    @Size(max = 50, message = "Email size is invalid")
    private String bookingEmail;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "StartTime cannot null")
    @Future(message = "StartTime must be a future date")
    private LocalDateTime eventStartTime;

    private Integer eventDuration;

    @Size(max = 500,message = "Notes size must between 0 and 500 character")
    private String eventNote;

    private EventCategoryDTO eventCategory;
//    private MultipartFile File;
    private String fileName;

    public String getEventCategoryName(EventDetailDTO eventDetailDTO) {
        return eventDetailDTO.getEventCategoryName();
    }
}