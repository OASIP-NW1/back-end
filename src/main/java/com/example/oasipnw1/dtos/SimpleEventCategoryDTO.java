package com.example.oasipnw1.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class SimpleEventCategoryDTO {
    private String eventCategoryName;
    private String eventCategoryDescription;
    private Integer eventDuration;

    public String getCategoryName() {
        return eventCategoryName;
    }
}
