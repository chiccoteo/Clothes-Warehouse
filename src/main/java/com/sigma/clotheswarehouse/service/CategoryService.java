package com.sigma.clotheswarehouse.service;

import com.sigma.clotheswarehouse.entity.Category;
import com.sigma.clotheswarehouse.payload.ApiResponse;
import com.sigma.clotheswarehouse.payload.CategoryGetDto;
import com.sigma.clotheswarehouse.payload.CategoryPostDto;
import com.sigma.clotheswarehouse.payload.CategoryUpdateDto;
import com.sigma.clotheswarehouse.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public ApiResponse addCategory(CategoryPostDto categoryPostDto) {
        Category category = new Category();
        if (categoryRepository.findByName(categoryPostDto.getName()).isPresent()){
            return new ApiResponse(false,"exist");
        }
        category.setName(categoryPostDto.getName());
        Category savedCategory = categoryRepository.save(category);
        return new ApiResponse(true,"success", savedCategory.getId());
    }

    public ApiResponse getById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()){
            return new ApiResponse(false,"not available");
        }
        Category category = optionalCategory.get();
        CategoryGetDto categoryGetDto = new CategoryGetDto();
        categoryGetDto.setId(id);
        categoryGetDto.setName(category.getName());
        return new ApiResponse(true,"available",categoryGetDto);
    }

    public ApiResponse getCategory() {
        List<CategoryGetDto> categoryGetDtos = new LinkedList<>();
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            CategoryGetDto categoryGetDto = new CategoryGetDto();
            categoryGetDto.setId(category.getId());
            categoryGetDto.setName(category.getName());
            categoryGetDtos.add(categoryGetDto);
        }
        return new ApiResponse(true,"success",categoryGetDtos);
    }

    public ApiResponse updateCategory(Long id, CategoryUpdateDto categoryUpdateDto) {
        if (!categoryRepository.existsById(id)){
            return new ApiResponse(false,"not available");
        }
        if (categoryRepository.findByIdAndName(id, categoryUpdateDto.getName()).isEmpty() &&
                categoryRepository.findByName(categoryUpdateDto.getName()).isPresent()){
            return new ApiResponse(false,"not available");
        }
        Category category = categoryRepository.findById(id).get();
        category.setName(categoryUpdateDto.getName());
        categoryRepository.save(category);
        return new ApiResponse(true,"available");
    }

//    public ApiResponse deleteCategory(Long id) {
//        return null;
//    }
}
