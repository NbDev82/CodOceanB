package com.example.codoceanb.admin.category.controller;

import com.example.codoceanb.admin.category.request.AddCategoryRequest;
import com.example.codoceanb.discuss.dto.CategoryDTO;
import com.example.codoceanb.discuss.dto.DiscussDTO;
import com.example.codoceanb.discuss.request.AddDiscussRequest;
import com.example.codoceanb.discuss.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/v1/category")
public class AdminCategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<CategoryDTO> addCategory(@ModelAttribute AddCategoryRequest request) {
        return ResponseEntity.ok(categoryService.add(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
