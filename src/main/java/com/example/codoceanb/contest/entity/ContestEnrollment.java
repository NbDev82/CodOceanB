package com.example.codoceanb.contest.entity;

import com.example.codoceanb.auth.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "contest_enrollments")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ContestEnrollment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private double score;

    @Column(name = "accepted_submission")
    private boolean acceptedSubmission;

    @Enumerated(EnumType.STRING)
    private EStatus status;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    public enum EStatus {
        PENDING_APPROVAL, ACCEPTED, DENIED, EXPIRED
    }
}
