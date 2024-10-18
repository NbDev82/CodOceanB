package com.example.codoceanb.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccessTokenResponse {
    @JsonProperty("accessToken")
    private String accessToken;
    @JsonProperty("message")
    private String message;
}
