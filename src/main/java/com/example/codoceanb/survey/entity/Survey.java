package com.example.codoceanb.survey.entity;

import com.example.codoceanb.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "answers")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "survey_data")
    private String surveyData;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

//    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
//    private List<Course> courses;
}
