package com.example.codoceanb.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgotPasswordResponse {
    @JsonProperty("message")
    private String message;
}
