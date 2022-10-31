package com.example.oasipnw1.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
public class EventDetailDTO {
    private Integer id;
    private String bookingName;
    private String bookingEmail;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "StartTime cannot null")
    private LocalDateTime eventStartTime;

    private Integer eventDuration;
    @Size(max = 500,message = "Notes size must between 0 and 500 character")
    private String eventNote;
    private Integer eventCategoryId;
    private String eventCategoryName;
}
