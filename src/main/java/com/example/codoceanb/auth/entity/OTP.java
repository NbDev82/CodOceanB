package com.example.codoceanb.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Otps")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name="encrypted_otp")
    private String encryptedOTP;

    @Column(name="expiration")
    private LocalDateTime expirationDate;

    private EType type;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;
    public enum EType {
        FORGOT_PASSWORD,
        ACTIVE_ACCOUNT,
        CHANGE_EMAIL
    }
}
