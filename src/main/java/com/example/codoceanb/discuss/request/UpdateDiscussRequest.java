package com.example.codoceanb.discuss.request;

import com.example.codoceanb.discuss.dto.CategoryDTO;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateDiscussRequest {
    private String title;
    private String description;
    private List<CategoryDTO> categories;
    private LocalDateTime endAt;
    private String image;
}
