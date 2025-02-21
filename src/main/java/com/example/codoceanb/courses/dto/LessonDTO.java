package com.example.codoceanb.courses.dto;

import com.example.codoceanb.courses.entity.PracticeLesson;
import com.example.codoceanb.courses.entity.TheoryLesson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class LessonDTO {
    private UUID id;
    private int index;
    private UUID courseId;
    private EType lessonType;

    // Field's TheoryLesson
    private String summary;
    private List<TheorySectionDTO> theorySections;

    // Field's PracticeLesson
    private UUID problemId;

    public TheoryLesson toTheoryEntity() {
        return TheoryLesson.builder()
                .id(this.id)
                .index(this.index)
                .summary(summary)
                .theorySections(theorySections
                        .stream()
                        .map(TheorySectionDTO::toEntity).toList())
                .build();
    }

    public PracticeLesson toPracticeEntity() {
        return PracticeLesson.builder()
                .id(this.id)
                .index(this.index)
                .problemId(problemId)
                .build();
    }

    public enum EType {
        THEORY, PRACTICE
    }
}