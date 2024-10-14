package com.example.codoceanb.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("refreshToken")
    private String refreshToken;
    @JsonProperty("message")
    private String message;
    @JsonProperty("isActive")
    private Boolean isActive;
    @JsonProperty("isFirstLogin")
    private Boolean isFirstLogin;

}
