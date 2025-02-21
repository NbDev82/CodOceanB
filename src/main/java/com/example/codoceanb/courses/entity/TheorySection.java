package com.example.codoceanb.courses.entity;

import com.example.codoceanb.courses.dto.TheorySectionDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "theory_sections")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TheorySection {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private int index;

    @ManyToOne
    @JoinColumn(name = "theory_lesson_id")
    private TheoryLesson theoryLesson;

    public TheorySectionDTO toDTO() {
        return TheorySectionDTO.builder()
                .id(id)
                .title(title)
                .content(content)
                .index(index)
                .build();
    }
}