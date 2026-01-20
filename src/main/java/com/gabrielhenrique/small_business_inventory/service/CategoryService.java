package com.gabrielhenrique.small_business_inventory.service;

import com.gabrielhenrique.small_business_inventory.dto.category.CategoryDTO;
import com.gabrielhenrique.small_business_inventory.exception.ResourceNotFoundException;
import com.gabrielhenrique.small_business_inventory.model.Category;
import com.gabrielhenrique.small_business_inventory.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream()
                .map(cat -> new CategoryDTO(cat.getId(), cat.getName()))
                .collect(Collectors.toList());
    }

    public CategoryDTO findById(Long id) {
        Category category = findEntityById(id);
        return new CategoryDTO(category.getId(), category.getName());
    }

    public Category findEntityById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Transactional
    public CategoryDTO create(CategoryDTO dto) {
        Category category = new Category();
        category.setName(dto.getName());
        category = categoryRepository.save(category);
        return new CategoryDTO(category.getId(), category.getName());
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category category = findEntityById(id);
        category.setName(dto.getName());
        category = categoryRepository.save(category);
        return new CategoryDTO(category.getId(), category.getName());
    }

    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}