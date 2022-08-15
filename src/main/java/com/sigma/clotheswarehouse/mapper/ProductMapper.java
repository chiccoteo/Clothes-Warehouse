package com.sigma.clotheswarehouse.mapper;

import com.sigma.clotheswarehouse.entity.Product;
import com.sigma.clotheswarehouse.payload.ProductDTO;
import com.sigma.clotheswarehouse.payload.ProductGetDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    @Mapping(target = "category.id", source = "categoryId")
    @Mapping(target = "measurement.id", source = "measurementId")
    Product toEntity(ProductDTO dto);


    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "measurementId", source = "measurement.id")
    ProductDTO toDTO(Product product);


    @Mapping(target = "categoryGetDto", source = "category")
    @Mapping(target = "measurementGetDto", source = "measurement")
    ProductGetDto getDTO(Product product);


    List<ProductGetDto> getDTOs(List<Product> productList);
}
