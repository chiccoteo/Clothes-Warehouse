package com.sigma.clotheswarehouse.mapper;

import com.sigma.clotheswarehouse.entity.OutcomeMaterial;
import com.sigma.clotheswarehouse.payload.OutcomeMaterialPostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface OutcomeMaterialMapper {

    @Mapping(target = "resources", ignore = true)
    @Mapping(target = "product", ignore = true)
    OutcomeMaterial toEntity(OutcomeMaterialPostDTO outcomeMaterialPostDTO);
}