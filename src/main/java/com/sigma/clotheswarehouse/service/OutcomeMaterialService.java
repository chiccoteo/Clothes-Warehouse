package com.sigma.clotheswarehouse.service;

import com.sigma.clotheswarehouse.entity.Material;
import com.sigma.clotheswarehouse.entity.OutcomeMaterial;
import com.sigma.clotheswarehouse.entity.Product;
import com.sigma.clotheswarehouse.entity.ResourceForOutcomeMaterial;
import com.sigma.clotheswarehouse.exceptions.PageSizeException;
import com.sigma.clotheswarehouse.mapper.OutcomeMaterialMapper;
import com.sigma.clotheswarehouse.mapper.ResourceForOutcomeMaterialMapper;
import com.sigma.clotheswarehouse.payload.ApiResponse;
import com.sigma.clotheswarehouse.payload.OutcomeMaterialGetDTO;
import com.sigma.clotheswarehouse.payload.OutcomeMaterialPostDTO;
import com.sigma.clotheswarehouse.repository.MaterialRepository;
import com.sigma.clotheswarehouse.repository.OutcomeMaterialRepository;
import com.sigma.clotheswarehouse.repository.ProductRepository;
import com.sigma.clotheswarehouse.repository.ResourceForOutcomeMaterialRepository;
import com.sigma.clotheswarehouse.utils.CommandUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OutcomeMaterialService {

    private final OutcomeMaterialRepository outcomeMaterialRepo;

    private final OutcomeMaterialMapper outcomeMaterialMapper;

    private final ResourceForOutcomeMaterialMapper resourceMapper;

    private final ProductRepository productRepo;

    private final ResourceForOutcomeMaterialRepository resourceForOutcomeMaterialRepo;

    private final MaterialRepository materialRepo;


    public ApiResponse addOutcomeMaterial(OutcomeMaterialPostDTO outcomeMaterialPostDTO) {
        OutcomeMaterial outcomeMaterial = outcomeMaterialMapper.toEntity(outcomeMaterialPostDTO);
        if (outcomeMaterial == null) {
            return new ApiResponse(false, "Such a product does not exist or outcome material is NULL");
        }
        List<ResourceForOutcomeMaterial> resourceForOutcomeMaterials = resourceMapper.toEntityList(outcomeMaterialPostDTO.getResources());
        if (resourceForOutcomeMaterials == null)
            return new ApiResponse(false, "Such material does not exist or there is not enough material");
        outcomeMaterial.setResources(resourceForOutcomeMaterials);
        OutcomeMaterial savedOutcomeMaterial = outcomeMaterialRepo.save(outcomeMaterial);
        return new ApiResponse(true, "Successfully saved", savedOutcomeMaterial.getId());
    }

    public ApiResponse getAllOutcomeMaterials(Integer page, Integer size) {
        Page<OutcomeMaterial> outcomeMaterialPage;
        try {
            outcomeMaterialPage = outcomeMaterialRepo.findAll(CommandUtils.simplePageable(page, size));
        } catch (PageSizeException e) {
            return new ApiResponse(false, e.getMessage());
        }
        List<OutcomeMaterialGetDTO> outcomeMaterialGetDTOList = outcomeMaterialMapper.toGetDTOList(outcomeMaterialPage.getContent());
        Map<String, Object> response = new HashMap<>();
        response.put("materials", outcomeMaterialGetDTOList);
        response.put("currentPage", outcomeMaterialPage.getNumber());
        response.put("totalItems", outcomeMaterialPage.getTotalElements());
        response.put("totalPages", outcomeMaterialPage.getTotalPages());
        return new ApiResponse(true, "All outcome materials", response);
    }

    public ApiResponse editOutcomeMaterialById(UUID id, OutcomeMaterialPostDTO outcomeMaterialUpdateDTO) {
        Optional<OutcomeMaterial> optionalOutcomeMaterial = outcomeMaterialRepo.findById(id);
        if (optionalOutcomeMaterial.isEmpty())
            return new ApiResponse(false, "Such a outcome material does not exist");
        OutcomeMaterial outcomeMaterial = optionalOutcomeMaterial.get();
        List<ResourceForOutcomeMaterial> resourceForOutcomeMaterials = resourceMapper.toUpdateEntityList(outcomeMaterial, outcomeMaterialUpdateDTO.getResources());
        if (resourceForOutcomeMaterials == null)
            return new ApiResponse(false, "Such material does not exist or there is not enough material");
        else
            outcomeMaterial.setResources(resourceForOutcomeMaterials);
        Product product = outcomeMaterial.getProduct();
        product.setPrice(outcomeMaterial.getProductOldPrice());
        product.setAmount(product.getAmount() - outcomeMaterial.getProductAmount());
        productRepo.save(product);
        Optional<Product> optionalProduct = productRepo.findByIdAndDeletedFalse(outcomeMaterialUpdateDTO.getProductId());
        if (optionalProduct.isEmpty())
            return new ApiResponse(false, "Such a product does not exist");
        Product newProduct = optionalProduct.get();
        outcomeMaterial.setProductOldPrice(newProduct.getPrice());
        newProduct.setAmount(outcomeMaterialUpdateDTO.getProductAmount());
        newProduct.setPrice(outcomeMaterialUpdateDTO.getProductPrice());
        outcomeMaterial.setProduct(newProduct);
        outcomeMaterial.setProductNewPrice(outcomeMaterialUpdateDTO.getProductPrice());
        outcomeMaterial.setProductAmount(outcomeMaterialUpdateDTO.getProductAmount());
        outcomeMaterialRepo.save(outcomeMaterial);
        boolean isItOutcome;
        for (ResourceForOutcomeMaterial resourceForOutcomeMaterial : resourceForOutcomeMaterialRepo.findAll()) {
            isItOutcome = false;
            for (OutcomeMaterial outcomeMaterial1 : outcomeMaterialRepo.findAll()) {
                if (outcomeMaterial1.getResources().contains(resourceForOutcomeMaterial)) {
                    isItOutcome = true;
                    break;
                }
            }
            if (!isItOutcome)
                resourceForOutcomeMaterialRepo.delete(resourceForOutcomeMaterial);
        }
        return new ApiResponse(true, "Successfully updated");
    }

    public ApiResponse deleteOutcomeMaterialId(UUID id) {
        Optional<OutcomeMaterial> optionalOutcomeMaterial = outcomeMaterialRepo.findById(id);
        if (optionalOutcomeMaterial.isEmpty())
            return new ApiResponse(false, "Such an outcome material does not exist");
        OutcomeMaterial outcomeMaterial = optionalOutcomeMaterial.get();
        rollbackAfterDeletingOutcomeMaterial(outcomeMaterial);
        outcomeMaterialRepo.delete(outcomeMaterial);
        return new ApiResponse(true, "Successfully deleted");
    }

    private void rollbackAfterDeletingOutcomeMaterial(OutcomeMaterial outcomeMaterial) {
        List<Material> materials = new LinkedList<>();
        for (ResourceForOutcomeMaterial resource : outcomeMaterial.getResources()) {
            Material material = resource.getMaterial();
            material.setAmount(material.getAmount() + resource.getMaterialAmount());
            materials.add(material);
        }
        materialRepo.saveAll(materials);

        Product product = outcomeMaterial.getProduct();
        product.setPrice(outcomeMaterial.getProductOldPrice());
        product.setAmount(product.getAmount() - outcomeMaterial.getProductAmount());
        productRepo.save(product);
    }
}
