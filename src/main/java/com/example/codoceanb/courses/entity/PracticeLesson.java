package com.example.codoceanb.courses.entity;

import com.example.codoceanb.courses.dto.LessonDTO;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@DiscriminatorValue("PRACTICE")
@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PracticeLesson extends Lesson {
    @Column(name = "problem_id", nullable = false)
    private UUID problemId;

    @Override
    public LessonDTO toDTO() {
        return LessonDTO.builder()
                .id(this.getId())
                .index(this.getIndex())
                .lessonType(LessonDTO.EType.PRACTICE)
                .problemId(this.getProblemId())
                .build();
    }
}