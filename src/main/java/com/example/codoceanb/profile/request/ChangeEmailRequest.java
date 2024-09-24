package com.example.codoceanb.profile.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChangeEmailRequest {
    private String newEmail;
    private String otp;
}
