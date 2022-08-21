package com.sigma.clotheswarehouse.controller;

import com.sigma.clotheswarehouse.payload.ApiResponse;
import com.sigma.clotheswarehouse.payload.MeasurementGetDto;
import com.sigma.clotheswarehouse.payload.MeasurementPostDto;
import com.sigma.clotheswarehouse.service.MeasurementService;
import com.sigma.clotheswarehouse.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.BASE_PATH + "/measurement")
@CrossOrigin(maxAge = 3600)
public class MeasurementController {

    final private MeasurementService measurementService;

    @PostMapping("/add")
    public HttpEntity<?> addMeasurement(@RequestBody MeasurementPostDto measurementPostDto){
        ApiResponse apiResponse = measurementService.addMeasurement(measurementPostDto);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/getById{id}")
    public HttpEntity<?> getById(@PathVariable Long id){
        ApiResponse apiResponse = measurementService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/getAll")
    public HttpEntity<?> getAllMeasurement(){
        ApiResponse apiResponse = measurementService.getAllMeasurement();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

    @PutMapping("/update/{id}")
    public HttpEntity<?> updateMeasurement(@PathVariable Long id, @RequestParam String name){
        ApiResponse apiResponse = measurementService.updateMeasurement(id,name);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

    @DeleteMapping("/delete/{id}")
    public HttpEntity<?> deleteMeasurement(@PathVariable Long id){
        ApiResponse apiResponse = measurementService.deleteMeasurement(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }
}
