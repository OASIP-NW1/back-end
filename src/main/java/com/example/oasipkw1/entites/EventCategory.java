package com.example.oasipkw1.entites;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "EventCategory")
public class EventCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eventCategoryId", nullable = false)
    private Integer id;

    @Column(name = "eventCategoryName", nullable = false)
    private String eventCategoryName;

    @Column(name = "eventCategoryDescription")
    private String eventCategoryDescription;

    @Column(name = "eventDuration", nullable = false)
    private Integer eventDuration;

}