package com.sigma.clotheswarehouse.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;
import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductHistoryGetDTO {
    private UUID id;

    private ProductGetDto productGetDto;

    private double price;

    private double amount;

    private ClientGetDto clientGetDto;

    private Timestamp date;
}
