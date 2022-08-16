package com.sigma.clotheswarehouse.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientPostDto {
    private String fio;
    private String phoneNumber;
    private String address;
    private Double borrowAmount;
    private Timestamp beginDate;
    private Timestamp endDate;
}
