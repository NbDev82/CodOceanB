package com.example.codoceanb.admin.category.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCategoryRequest {
    private String name;
    private String description;
    private String imageUrl;
}
