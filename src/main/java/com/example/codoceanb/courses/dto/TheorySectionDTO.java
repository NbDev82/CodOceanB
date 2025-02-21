package com.example.codoceanb.courses.dto;

import com.example.codoceanb.courses.entity.TheorySection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TheorySectionDTO {
    private UUID id;
    private String title;
    private String content;
    private int index;

    public TheorySection toEntity() {
        return TheorySection.builder()
                .id(this.id)
                .title(this.title)
                .content(this.content)
                .index(this.index)
                .build();
    }
}