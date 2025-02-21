package com.example.codoceanb.courses.entity;

import com.example.codoceanb.courses.dto.LessonDTO;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "lessons")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "lesson_type")
public abstract class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private int index;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public abstract LessonDTO toDTO();
}