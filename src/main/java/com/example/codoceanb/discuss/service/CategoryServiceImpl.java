package com.example.codoceanb.discuss.service;

import com.example.codoceanb.admin.category.request.AddCategoryRequest;
import com.example.codoceanb.auth.entity.User;
import com.example.codoceanb.discuss.dto.CategoryDTO;
import com.example.codoceanb.discuss.dto.DiscussDTO;
import com.example.codoceanb.discuss.entity.Category;
import com.example.codoceanb.discuss.repository.CategoryRepository;
import com.example.codoceanb.uploadfile.service.UploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UploadFileService uploadFileService;

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return convertToCategoryDTOs(categories);
    }

    @Override
    public CategoryDTO add(AddCategoryRequest request) {
        String imgUrl = uploadFileService.uploadImage(request.getImage(), User.ERole.ADMIN.name());

        Category category = Category.builder()
                .name(request.getName())
                .imageUrl(imgUrl)
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
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .build();
    }

    private List<CategoryDTO> convertToCategoryDTOs(List<Category> categories) {
        return categories.stream()
                         .map(this::convertToCategoryDTO)
                         .toList();
    }
}
