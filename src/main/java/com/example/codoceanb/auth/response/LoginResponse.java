package com.example.codoceanb.auth.response;

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
