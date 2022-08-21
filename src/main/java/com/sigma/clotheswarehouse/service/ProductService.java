package com.sigma.clotheswarehouse.service;

import com.sigma.clotheswarehouse.entity.Category;
import com.sigma.clotheswarehouse.entity.Measurement;
import com.sigma.clotheswarehouse.entity.Product;
import com.sigma.clotheswarehouse.mapper.ProductMapper;
import com.sigma.clotheswarehouse.payload.ApiResponse;
import com.sigma.clotheswarehouse.payload.ProductDTO;
import com.sigma.clotheswarehouse.payload.ProductGetDto;
import com.sigma.clotheswarehouse.repository.CategoryRepository;
import com.sigma.clotheswarehouse.repository.MeasurementRepository;
import com.sigma.clotheswarehouse.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final MeasurementRepository measurementRepository;

    private final ProductMapper mapper;

    private final CategoryRepository categoryRepository;

    public HttpEntity<?> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Product> productPage = repository.findAllByDeletedFalse(pageable);
        List<ProductGetDto> DTOs = mapper.getDTOs(productPage.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("products", DTOs);
        response.put("currentPage", productPage.getNumber());
        response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());
        return ResponseEntity.ok(response);
    }

    public HttpEntity<?> getById(UUID id) {
        Optional<Product> optionalProduct = repository.findByIdAndDeletedFalse(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            ProductGetDto productGetDto = mapper.getDTO(product);
            return ResponseEntity.status(200).body(productGetDto);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, "PRODUCT_NOT_FOUND"));
    }

    public HttpEntity<?> edit(UUID id, ProductDTO dto) {
        Optional<Product> optionalProduct = repository.findByIdAndDeletedFalse(id);
        if (optionalProduct.isPresent()) {
            if (dto.getMeasurementId() != null) {
                Optional<Measurement> optionalMeasurement = measurementRepository.findById(dto.getMeasurementId());
                if (optionalMeasurement.isPresent()) {
                    Optional<Category> byId = categoryRepository.findById(dto.getCategoryId());
                    if (byId.isPresent()) {
                        Category category = byId.get();
                        Measurement measurement = optionalMeasurement.get();
                        Product product = optionalProduct.get();
                        product.setName(dto.getName());
                        product.setPrice(dto.getPrice());
                        product.setAmount(dto.getAmount());
                        product.setMeasurement(measurement);
                        product.setDeleted(dto.isDeleted());
                        product.setCategory(category);
                        product.setModel(dto.getModel());
                        product.setCode(dto.getCode());
                        product.setColor(dto.getColor());
                        product.setSeriaAmount(dto.getSeriaAmount());
                        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "PRODUCT_EDITED"));
                    }
                    return ResponseEntity.status(400).body(new ApiResponse(false, "CATEGORY_NOT_FOUND"));
                }
                return ResponseEntity.status(400).body(new ApiResponse(false, "MEASUREMENT_NOT_FOUND"));
            }
        }
        return ResponseEntity.status(400).body(new ApiResponse(false, "PRODUCT_NOT_FOUND"));
    }


    public HttpEntity<?> delete(UUID id) {
        Optional<Product> optionalProduct = repository.findByIdAndDeletedFalse(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setDeleted(true);
            repository.save(product);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "PRODUCT_DELETED"));
        }
        return ResponseEntity.status(400).body(new ApiResponse(false, "PRODUCT_NOT_FOUND"));
    }

    public HttpEntity<?> create(ProductDTO dto) {
        if (dto.getCategoryId() != null && dto.getMeasurementId() != null) {
            Product product = mapper.toEntity(dto);
            Product savedProduct = repository.save(product);
            return ResponseEntity.status(HttpStatus.OK).
                    body(new ApiResponse
                            (true, "PRODUCT_SAVED",
                                    savedProduct.getId()));
        }
        return ResponseEntity.status(400).body(new ApiResponse(false, "CATEGORY_OR_MEASUREMENT_NOT_FOUND"));
    }

    public HttpEntity<?> getAllWithoutPage() {
        return ResponseEntity.ok(mapper.getDTOs(repository.findAll()));
    }
}
