package com.sigma.clotheswarehouse.controller;

import com.sigma.clotheswarehouse.payload.*;
import com.sigma.clotheswarehouse.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sigma.clotheswarehouse.utils.AppConstant.BASE_PATH;

@RestController
@RequiredArgsConstructor
@RequestMapping(BASE_PATH + "/category")
@CrossOrigin(maxAge = 3600)
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public HttpEntity<?> addCategory(@RequestBody CategoryPostDto categoryPostDto){
        ApiResponse apiResponse = categoryService.addCategory(categoryPostDto);
        return ResponseEntity.ok(apiResponse);
    }
    @GetMapping("/getById/{id}")
    public HttpEntity<?> getCategoryById(@PathVariable Long id){
        ApiResponse apiResponse = categoryService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }
    @GetMapping("/getAll")
    public HttpEntity<?> getCategory(){
        ApiResponse apiResponse = categoryService.getCategory();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }
    @PutMapping("/update/{id}")
    public HttpEntity<?> updateCategory(@PathVariable Long id, @RequestBody CategoryUpdateDto categoryUpdateDto){
        ApiResponse apiResponse = categoryService.updateCategory(id, categoryUpdateDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }
//    @DeleteMapping("/delete/{id}")
//    public HttpEntity<?> deleteCategory(@PathVariable Long id){
//        ApiResponse apiResponse = categoryService.deleteCategory(id);
//        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
//    }
}
