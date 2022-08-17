package com.example.oasipnw1.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class EventCategoryDTO {

    @NotNull(message = "ID cannot null")
    private Integer id;

    @NotBlank(message = "eventCategoryName cannot be empty")
    @Size(max = 100, message = "eventCategoryName Must not exceed 100 characters.")
    private String eventCategoryName;

    private String eventCategoryDescription;

    private Integer eventDuration;

    public String getCategoryName() {
        return eventCategoryName;
    }
}
