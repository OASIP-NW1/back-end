package com.example.oasipnw1.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class SimpleEventDTO {
    private Integer id;
    private String bookingName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private ZonedDateTime eventStartTime;

    private Integer eventDuration;
    @JsonIgnore
    private SimpleEventCategoryDTO eventCategory;

    public String getEventCategoryName() {

        return eventCategory.getEventCategoryName();
    }
    private String bookingEmail;


}
