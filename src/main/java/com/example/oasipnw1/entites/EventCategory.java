package com.example.oasipnw1.entites;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.validation.constraints.*;

@Getter
@Setter
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

    public String getEventCategoryNames() {
        return eventCategoryName;
    }

    public void setEventCategoryName(String eventCategoryName) {
        this.eventCategoryName = eventCategoryName;
    }

    @Column(name = "eventDuration", nullable = false)
    private Integer eventDuration;
}