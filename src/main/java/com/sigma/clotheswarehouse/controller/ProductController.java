package com.sigma.clotheswarehouse.controller;

import com.sigma.clotheswarehouse.payload.ProductDTO;
import com.sigma.clotheswarehouse.service.ProductService;
import com.sigma.clotheswarehouse.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(AppConstant.BASE_PATH + "/product")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{page}/{size}")
    public HttpEntity<?> getAll(@PathVariable int page, @PathVariable int size) {
        return productService.getAll(page,size);
    }

    @GetMapping("/all")
    public HttpEntity<?> getAllWithoutPage() {
        return productService.getAllWithoutPage();
    }

    @GetMapping("/{id}")
    public HttpEntity<?> getById(@PathVariable UUID id) {
        return productService.getById(id);
    }



    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id, @RequestBody ProductDTO dto) {
        return productService.edit(id,dto);
    }

    @PostMapping
    public HttpEntity<?> create(@RequestBody ProductDTO dto){
        return productService.create(dto);
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable UUID id){
        return productService.delete(id);
    }
}

