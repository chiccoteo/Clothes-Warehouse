package com.sigma.clotheswarehouse.payload;

import com.sigma.clotheswarehouse.entity.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductGetDto {
    private UUID id;

    private String name;

    private String model;

    private String code;

    private String color;

    private double seriaAmount;

    private double price;

    private double amount;

    private MeasurementGetDto measurementGetDto;

    private CategoryGetDto categoryGetDto;

    private boolean deleted;
}
