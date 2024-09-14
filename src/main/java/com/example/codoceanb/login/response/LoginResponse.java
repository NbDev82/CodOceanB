package com.example.codoceanb.login.response;

import com.example.codoceanb.login.dto.UserDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    @JsonProperty("token")
    private String token;
    @JsonProperty("message")
    private String message;
    @JsonProperty("isActive")
    private Boolean isActive;
}
