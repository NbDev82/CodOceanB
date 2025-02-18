package com.example.codoceanb.discuss.service;

import com.example.codoceanb.admin.category.request.AddCategoryRequest;
import com.example.codoceanb.discuss.dto.CategoryDTO;
import com.example.codoceanb.discuss.dto.DiscussDTO;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();

    CategoryDTO add(AddCategoryRequest request);

    void deleteCategory(UUID id);
}
