package com.example.oasipnw1.entites;

import com.example.oasipnw1.dtos.EventDetailDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "Event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookingId", nullable = false)
    private Integer id;

    @NotBlank(message = "Booking Name is not empty")
    @Size(max = 100,message = "Booking Name size must between 0 and 100 character")
    @Column(name = "bookingName", nullable = false)
    private String bookingName;

    @Email(message = "Email must be a well-formed email address" ,regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @NotBlank(message = "Email cannot be empty")
    @Size(max = 50, message = "Email size is invalid")
    @Column(name = "bookingEmail" ,length = 50)
    private String bookingEmail;

    @NotNull(message = "StartTime cannot null")
    @Future(message = "StartTime must be a future date")
    @Column(name = "eventStartTime", nullable = false)
    private LocalDateTime eventStartTime;

    @Size(max = 500,message = "Notes size must between 0 and 500 character")
    @Column(name = "eventNote")
    private String eventNote;

    @Column(name = "eventDuration", nullable = false)
    private Integer eventDuration;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "eventCategoryId", nullable = false)
    private EventCategory eventCategory;

    public String getEventCategoryName(Event event) {

        return event.getEventCategoryName(event);
    }

}