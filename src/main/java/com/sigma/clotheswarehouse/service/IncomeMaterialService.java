package com.sigma.clotheswarehouse.service;

import com.sigma.clotheswarehouse.entity.IncomeMaterial;
import com.sigma.clotheswarehouse.entity.Material;
import com.sigma.clotheswarehouse.entity.Measurement;
import com.sigma.clotheswarehouse.exceptions.PageSizeException;
import com.sigma.clotheswarehouse.mapper.IncomeMaterialMapper;
import com.sigma.clotheswarehouse.mapper.MaterialMapper;
import com.sigma.clotheswarehouse.mapper.MeasurementMapper;
import com.sigma.clotheswarehouse.payload.ApiResponse;
import com.sigma.clotheswarehouse.payload.IncomeMaterialDTO;
import com.sigma.clotheswarehouse.repository.IncomeMaterialRepository;
import com.sigma.clotheswarehouse.repository.MaterialRepository;
import com.sigma.clotheswarehouse.repository.MeasurementRepository;
import com.sigma.clotheswarehouse.utils.CommandUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class IncomeMaterialService {

    private final MaterialMapper materialMapper;

    private final MeasurementMapper measurementMapper;

    private final MaterialRepository materialRepository;

    private final MeasurementRepository measurementRepository;

    private final IncomeMaterialRepository incomeMaterialRepo;

    private final IncomeMaterialMapper incomeMaterialMapper;


    public ApiResponse addIncomeMaterial(List<IncomeMaterialDTO> incomeMaterialDTOList) {
        List<IncomeMaterial> incomeMaterialList = new LinkedList<>();
        for (IncomeMaterialDTO incomeMaterialDTO : incomeMaterialDTOList) {
            if (incomeMaterialDTO.getAmount() < 0)
                return new ApiResponse(false, "The amount of material must not be negative");
            IncomeMaterial incomeMaterial = incomeMaterialMapper.toEntity(incomeMaterialDTO);

            Material material = materialMapper.toEntity(incomeMaterialDTO.getMaterialPostDTO());

            Measurement measurement = measurementMapper.toEntity(incomeMaterialDTO.getMaterialPostDTO().getMeasurementDTO());
            Optional<Measurement> optionalMeasurement = measurementRepository.findByName(measurement.getName());
            if (optionalMeasurement.isPresent())
                material.setMeasurement(optionalMeasurement.get());
            else
                material.setMeasurement(measurementRepository.save(measurement));

            Optional<Material> optionalMaterial = materialRepository.findByName(material.getName());
            if (optionalMaterial.isPresent()) {
                Material material1 = optionalMaterial.get();
                material1.setAmount(material1.getAmount() + incomeMaterialDTO.getAmount());
                material1.setPrice(incomeMaterialDTO.getPrice());
                incomeMaterial.setMaterial(material1);
            } else {
                material.setAmount(incomeMaterialDTO.getAmount());
                material.setPrice(incomeMaterialDTO.getPrice());
                incomeMaterial.setMaterial(materialRepository.save(material));
            }

            incomeMaterialList.add(incomeMaterial);
        }
        List<IncomeMaterial> incomeMaterials = incomeMaterialRepo.saveAll(incomeMaterialList);
        List<UUID> incomeMaterialsIds = new LinkedList<>();
        incomeMaterials.forEach(incomeMaterial -> incomeMaterialsIds.add(incomeMaterial.getId()));
        return new ApiResponse(true, "Successfully saved", incomeMaterialsIds);
    }

    public ApiResponse getAllIncomeMaterials(Integer page, Integer size) {
        Page<IncomeMaterial> incomeMaterialPage;
        try {
            incomeMaterialPage = incomeMaterialRepo.findAll(CommandUtils.simplePageable(page, size));
        } catch (PageSizeException e) {
            return new ApiResponse(false, e.getMessage());
        }
        List<IncomeMaterialDTO> incomeMaterialGetDTOS = incomeMaterialMapper.toDTOList(incomeMaterialPage.getContent());
        Map<String, Object> response = new HashMap<>();
        response.put("incomeMaterials", incomeMaterialGetDTOS);
        response.put("currentPage", incomeMaterialPage.getNumber());
        response.put("totalItems", incomeMaterialPage.getTotalElements());
        response.put("totalPages", incomeMaterialPage.getTotalPages());
        return new ApiResponse(true, "Income materials with Page", response);
    }

    public ApiResponse getIncomeMaterialsBetweenTimes(Integer page, Integer size, Timestamp startDate, Timestamp endDate) {
        Pageable pageable;
        try {
            pageable = CommandUtils.simplePageable(page, size);
        } catch (PageSizeException e) {
            return new ApiResponse(false, e.getMessage());
        }
        Page<IncomeMaterial> incomeMaterialRepoAllByIncomeDateBetween = incomeMaterialRepo.findAllByIncomeDateBetween(startDate, endDate, pageable);
        List<IncomeMaterialDTO> incomeMaterialGetDTOS = incomeMaterialMapper.toDTOList(incomeMaterialRepoAllByIncomeDateBetween.getContent());
        Map<String, Object> response = new HashMap<>();
        response.put("incomeMaterials", incomeMaterialGetDTOS);
        response.put("currentPage", incomeMaterialRepoAllByIncomeDateBetween.getNumber());
        response.put("totalItems", incomeMaterialRepoAllByIncomeDateBetween.getTotalElements());
        response.put("totalPages", incomeMaterialRepoAllByIncomeDateBetween.getTotalPages());
        return new ApiResponse(true, "Income materials between times", response);
    }
}
