package com.gabrielhenrique.small_business_inventory.service;

import com.gabrielhenrique.small_business_inventory.dto.category.CategoryDTO;
import com.gabrielhenrique.small_business_inventory.exception.ResourceNotFoundException;
import com.gabrielhenrique.small_business_inventory.model.Category;
import com.gabrielhenrique.small_business_inventory.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        categoryDTO = new CategoryDTO(1L, "Electronics");
    }

    @Test
    void testFindAll() {
        when(categoryRepository.findAll()).thenReturn(Collections.singletonList(category));

        List<CategoryDTO> result = categoryService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(categoryDTO.getName(), result.get(0).getName());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryDTO result = categoryService.findById(1L);

        assertNotNull(result);
        assertEquals(categoryDTO.getName(), result.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.findById(1L);
        });

        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    void testCreate() {
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        CategoryDTO result = categoryService.create(categoryDTO);

        assertNotNull(result);
        assertEquals(categoryDTO.getName(), result.getName());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdate_Success() {
        CategoryDTO updatedDto = new CategoryDTO(1L, "Home Appliances");
        Category updatedCategory = new Category();
        updatedCategory.setId(1L);
        updatedCategory.setName("Home Appliances");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        CategoryDTO result = categoryService.update(1L, updatedDto);

        assertNotNull(result);
        assertEquals("Home Appliances", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdate_NotFound() {
        CategoryDTO updatedDto = new CategoryDTO(1L, "Home Appliances");
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.update(1L, updatedDto);
        });

        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testDelete_Success() {
        when(categoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(categoryRepository).deleteById(1L);

        categoryService.delete(1L);

        verify(categoryRepository, times(1)).existsById(1L);
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.delete(1L);
        });

        verify(categoryRepository, times(1)).existsById(1L);
        verify(categoryRepository, never()).deleteById(1L);
    }
}
