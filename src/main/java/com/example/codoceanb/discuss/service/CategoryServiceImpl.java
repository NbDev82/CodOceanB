package com.example.codoceanb.discuss.service;

import com.example.codoceanb.admin.category.request.AddCategoryRequest;
import com.example.codoceanb.discuss.dto.CategoryDTO;
import com.example.codoceanb.discuss.dto.DiscussDTO;
import com.example.codoceanb.discuss.entity.Category;
import com.example.codoceanb.discuss.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return convertToCategoryDTOs(categories);
    }

    @Override
    public CategoryDTO add(AddCategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .imageUrl(request.getImageUrl())
                .description(request.getDescription())
                .build();
        categoryRepository.save(category);
        return convertToCategoryDTO(category);
    }

    @Override
    public void deleteCategory(UUID id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDTO convertToCategoryDTO(Category category) {
        return CategoryDTO.builder()
                         .name(category.getName())
                         .description(category.getDescription())
                         .build();
    }

    private List<CategoryDTO> convertToCategoryDTOs(List<Category> categories) {
        return categories.stream()
                         .map(category -> new CategoryDTO(category.getName(), category.getDescription()))
                         .toList();
    }
}
