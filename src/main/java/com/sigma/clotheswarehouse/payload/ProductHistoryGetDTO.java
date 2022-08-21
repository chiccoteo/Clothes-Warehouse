package com.sigma.clotheswarehouse.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Timestamp;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductHistoryGetDTO {
    private ProductGetDto productGetDto;

    private double price;

    private double amount;

    private ClientGetDto clientGetDto;

    private Timestamp date;
}
