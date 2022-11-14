package com.example.oasipnw1.entites;

import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.validation.constraints.*;

//@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Service
@Table(name = "EventCategory")
public class EventCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventCategoryId", nullable = false)
    private Integer id;

    @NotBlank(message = "eventCategoryName cannot be empty")
    @Size(max = 100, message = "eventCategoryName Must not exceed 100 characters.")
    @Column(name = "eventCategoryName", nullable = false)
    public String eventCategoryName;

    private String eventCategoryDescription;

    @Column(name = "eventDuration", nullable = false)
    private Integer eventDuration;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventCategoryName() {
        return eventCategoryName;
    }

    public void setEventCategoryName(String eventCategoryName) {
        this.eventCategoryName = eventCategoryName;
    }

    public String getEventCategoryDescription() {
        return eventCategoryDescription;
    }

    public void setEventCategoryDescription(String eventCategoryDescription) {
        this.eventCategoryDescription = eventCategoryDescription;
    }

    public Integer getEventDuration() {
        return eventDuration;
    }

    public void setEventDuration(Integer eventDuration) {
        this.eventDuration = eventDuration;
    }
}