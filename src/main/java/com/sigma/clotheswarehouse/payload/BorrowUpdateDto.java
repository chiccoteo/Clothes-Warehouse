package com.sigma.clotheswarehouse.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowUpdateDto {
    private Long clientId;
    private Double amount;
    private Timestamp beginDate;
    private Timestamp endDate;
}
