package com.sigma.clotheswarehouse.repository;

import com.sigma.clotheswarehouse.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Override
    Page<Product> findAll(Pageable pageable);
}
