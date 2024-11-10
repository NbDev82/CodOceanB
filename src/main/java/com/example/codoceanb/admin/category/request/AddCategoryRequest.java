package com.example.codoceanb.admin.category.request;

import lombok.*;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.Required;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCategoryRequest {
    private String name;
    private String description;
    private MultipartFile image;
}
