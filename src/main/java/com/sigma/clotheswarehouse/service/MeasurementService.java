package com.sigma.clotheswarehouse.service;

import com.sigma.clotheswarehouse.entity.Measurement;
import com.sigma.clotheswarehouse.payload.ApiResponse;
import com.sigma.clotheswarehouse.payload.MeasurementGetDto;
import com.sigma.clotheswarehouse.payload.MeasurementPostDto;
import com.sigma.clotheswarehouse.repository.MeasurementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MeasurementService {

    final private MeasurementRepository measurementRepository;
    public ApiResponse addMeasurement(MeasurementPostDto measurementPostDto) {
        Optional<Measurement> measurementOptional = measurementRepository.findByName(measurementPostDto.getName());
        if (measurementOptional.isPresent()){
            return new ApiResponse(false, "Measurement's name is exist");
        }
        Measurement measurement = new Measurement(measurementPostDto.getName(), false);
        Measurement savedMeasurement = measurementRepository.save(measurement);
        return new ApiResponse(true, "Measurement successfully saved", savedMeasurement.getId());
    }



    public ApiResponse getById(Long id) {
        Optional<Measurement> measurementOptional = measurementRepository.findById(id);
        if (measurementOptional.isEmpty()){
            return new ApiResponse(false,"Such a measurement does not exist");
        }
        Measurement measurement = measurementOptional.get();
        MeasurementGetDto measurementGetDto = new MeasurementGetDto(measurement.getId(), measurement.getName(), measurement.isDeleted());
        return new ApiResponse(true,"Success",measurementGetDto);
    }


    public ApiResponse getAllMeasurement() {
        List<Measurement> measurementList = measurementRepository.findAll();
        List<MeasurementGetDto> measurementGetDtoList = new LinkedList<>();
        for (Measurement measurement : measurementList) {
            MeasurementGetDto measurementGetDto1 = new MeasurementGetDto();
            measurementGetDto1.setId(measurement.getId());
            measurementGetDto1.setName(measurement.getName());
            measurementGetDto1.setDeleted(measurement.isDeleted());
            measurementGetDtoList.add(measurementGetDto1);
        }
        return new ApiResponse(true, "All Measurements",measurementGetDtoList);
    }


    public ApiResponse updateMeasurement(Long id, String name) {
        Optional<Measurement> measurementOptional = measurementRepository.findById(id);
        if (measurementOptional.isEmpty()){
            return new ApiResponse(false,"Such a measurement does not exist");
        }
        Optional<Measurement> measurementOptionalByName = measurementRepository.findByName(name);
        if (measurementOptionalByName.isPresent()){
            return new ApiResponse(false, "Measurement's name is exist");
        }
        Measurement measurement = measurementOptional.get();
        measurement.setName(name);
        measurementRepository.save(measurement);
        return new ApiResponse(true,"Measurement successfully updated");
    }

    public ApiResponse deleteMeasurement(Long id) {
        Optional<Measurement> measurementOptional = measurementRepository.findById(id);
        if (measurementOptional.isEmpty()){
            return new ApiResponse(false,"Such a measurement does not exist");
        }
        Measurement measurement = measurementOptional.get();
        measurement.setDeleted(true);
        measurementRepository.save(measurement);
        return new ApiResponse(true,"Measurement successfully deleted");
    }
}
