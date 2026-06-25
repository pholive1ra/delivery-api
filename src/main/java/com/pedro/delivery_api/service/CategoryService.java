package com.pedro.delivery_api.service;

import com.pedro.delivery_api.dto.CategoryRequestDTO;
import com.pedro.delivery_api.dto.CategoryResponseDTO;
import com.pedro.delivery_api.entity.Category;
import com.pedro.delivery_api.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDTO create(CategoryRequestDTO request) {
        Category category = new Category();

        category.setName(request.name());
        category.setActive(true);

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponseDTO(savedCategory.getId(), savedCategory.getName(), savedCategory.getActive());
    }

    public List<CategoryResponseDTO> list() {
        List<Category> listCategory = categoryRepository.findAll();

        return listCategory.stream()
                .map(c -> new CategoryResponseDTO(c.getId(), c.getName(), c.getActive()))
                .toList();
    }

    public CategoryResponseDTO listById(Long id){
    Category listCategoryById = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Categoria não encontrada."));

        return new CategoryResponseDTO(listCategoryById.getId(), listCategoryById.getName(), listCategoryById.getActive());
    }

    public CategoryResponseDTO updateCategory(Long categoryId, CategoryRequestDTO request) {
        Category listCategoryById = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Categoria não encontrada."));
            listCategoryById.setName(request.name());

            Category savedCategoryUpdated = categoryRepository.save(listCategoryById);

            return new CategoryResponseDTO(savedCategoryUpdated.getId(), savedCategoryUpdated.getName(), savedCategoryUpdated.getActive());
    }

    public void deleteCategory(Long categoryId) {
        Category listCategoryById = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Categoria não encontrada."));
        categoryRepository.deleteById(categoryId);
    }
}