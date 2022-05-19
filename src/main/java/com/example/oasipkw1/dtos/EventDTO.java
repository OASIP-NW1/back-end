package com.example.oasipkw1.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.*;
import java.time.Instant;
import java.time.ZonedDateTime;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class EventDTO {
    private Integer id;

    @NotBlank(message = "BookingName is not empty")
    @Size(max = 100,message = "BookingName size is invalied")
    private String bookingName;

    @Email(message = "Email is not valid" ,regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @NotBlank(message = "Email cannot be empty")
    @Size(max = 50, message = "Email size is invalied")
    private String bookingEmail;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Bangkok")
    @NotNull(message = "StartTime cannot null")
    @Future(message = "Required future date time")
    private ZonedDateTime eventStartTime;

    private Integer eventDuration;
    private String eventNote;
    private EventCategoryDTO eventCategory;
    public String getEventCategoryName() {
        return eventCategory.getEventCategoryName();
    }
}