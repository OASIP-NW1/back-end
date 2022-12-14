package com.example.oasipnw1.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponse {
//    private String jwtToken;
    private String message;
    private String accessToken;
    private String refreshToken;
}
