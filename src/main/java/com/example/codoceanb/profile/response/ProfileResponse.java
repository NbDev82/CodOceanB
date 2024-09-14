package com.example.codoceanb.profile.response;

import com.example.codoceanb.login.dto.UserDTO;
import com.example.codoceanb.profile.dto.ProfileDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {
    @JsonProperty("profile")
    private ProfileDTO profile;
}
