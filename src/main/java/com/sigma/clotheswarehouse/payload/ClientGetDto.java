package com.sigma.clotheswarehouse.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientGetDto {
    private Long id;
    private String fio;
    private String phoneNumber;
    private String address;
    private Double borrowAmount;
    private Timestamp beginDate;
    private Timestamp endDate;
    private boolean deleted;
}
