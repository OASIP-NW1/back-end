package com.example.oasipkw1.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimpleEventCategoryDTO {
    private String eventCategoryName;
    private String eventCategoryDescription;
    private Integer eventDuration;

    public String getCategoryName() {
        return eventCategoryName;
    }
}
