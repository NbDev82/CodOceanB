package com.example.codoceanb.auth.dto;

import com.example.codoceanb.auth.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserDTO {
    private UUID id;

    private String fullName;

    private String phoneNumber;

    private LocalDateTime dateOfBirth;

    @NotBlank(message = "Email number is required")
    private String email;
    private String urlImage;
    private String password;

    private double cumulativeScore;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @NotNull(message = "Role ID is required")
    private User.ERole role;
}
