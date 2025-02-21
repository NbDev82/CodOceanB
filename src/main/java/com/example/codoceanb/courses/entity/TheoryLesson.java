package com.example.codoceanb.courses.entity;

import com.example.codoceanb.courses.dto.LessonDTO;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@DiscriminatorValue("THEORY")
@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TheoryLesson extends Lesson {

    @Column(nullable = false)
    private String summary;

    @OneToMany(mappedBy = "theoryLesson", cascade = CascadeType.ALL)
    private List<TheorySection> theorySections;

    @Override
    public LessonDTO toDTO() {
        return LessonDTO.builder()
                .id(this.getId())
                .index(this.getIndex())
                .lessonType(LessonDTO.EType.THEORY)
                .summary(this.getSummary())
                .theorySections(this.getTheorySections().stream().map(TheorySection::toDTO).toList())
                .build();
    }
}
