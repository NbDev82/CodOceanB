package com.example.codoceanb.courses.dto;

import com.example.codoceanb.courses.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDTO {
    private UUID id;
    private String title;
    private String description;
    private Course.ELevel level;
    private int duration;
    private Double price;
    private Course.ECurrency currency;
    private boolean isPublic;
    private List<LessonDTO> lessons;

    public Course toEntity() {
        return Course.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .level(this.level)
                .duration(duration)
                .price(price)
                .currency(currency)
                .build();
    }
}
