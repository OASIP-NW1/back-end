package com.example.oasipnw1.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

@Getter
@Setter
public class EventUpdateDTO {

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "StartTime cannot null")
    @Future(message = "StartTime must be a future date")
    private ZonedDateTime eventStartTime;

    @Size(max = 500,message = "Notes size must between 0 and 500 character")
    private String eventNote;
}