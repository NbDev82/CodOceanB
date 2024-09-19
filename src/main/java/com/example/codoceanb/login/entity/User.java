package com.example.codoceanb.login.entity;

import com.example.codoceanb.comment.entity.Comment;
import com.example.codoceanb.contest.entity.Contest;
import com.example.codoceanb.contest.entity.ContestEnrollment;
import com.example.codoceanb.notification.entity.Notification;
import com.example.codoceanb.submitcode.problem.entity.Problem;
import com.example.codoceanb.submitcode.submission.entity.Submission;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "date_of_birth")
    private LocalDateTime dateOfBirth;

    private String email;
    @Column(length = 1000)
    private String urlImage;
    private String password;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    private ERole role;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Problem> ownedProblems;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ContestEnrollment> contestEnrollments;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Contest> contests;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Token> tokens;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Submission> submissions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<OTP> otps;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_"+role.name().toUpperCase()));
        return authorityList;
    }
    @Override
    public String getUsername() {
        return fullName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum ERole {
        USER(0),
        ADMIN(1);
        private final int value;
        ERole(int value) {
            this.value = value;
        }
    }
}
