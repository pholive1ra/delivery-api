package com.pedro.delivery_api.controller;

import com.pedro.delivery_api.dto.CategoryRequestDTO;
import com.pedro.delivery_api.dto.CategoryResponseDTO;
import com.pedro.delivery_api.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("")
    public CategoryResponseDTO create(@RequestBody CategoryRequestDTO request) {
       return categoryService.create(request);
    }

    @GetMapping("/{id}")
    public CategoryResponseDTO listById(@PathVariable Long id) {
        return categoryService.listById(id);
    }

    @GetMapping("")
    public List<CategoryResponseDTO> list() {
       return categoryService.list();
    }

    @PutMapping("/{id}")
    public CategoryResponseDTO update(@PathVariable Long id, @RequestBody CategoryRequestDTO request) {
        return categoryService.updateCategory(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}


