package com.example.codoceanb.courses.entity;

import com.example.codoceanb.courses.dto.CourseDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "courses")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ELevel level;
    
    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int duration;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ECurrency currency;

    @Column(nullable = false)
    private boolean isPublic = false;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Lesson> lessons;

    public CourseDTO toDTO() {
        return CourseDTO.builder()
                .id(id)
                .title(title)
                .description(description)
                .level(level)
                .price(price)
                .duration(duration)
                .currency(currency)
                .isPublic(isPublic)
                .lessons(lessons.stream().map(Lesson::toDTO).toList())
                .build();
    }

    public enum ECurrency {
        USD, VND, JPY, KRW
    }
    
    public enum ELevel {
        BEGINNER, INTERMEDIATE, ADVANCED
    }

}