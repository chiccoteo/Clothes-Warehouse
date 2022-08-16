package com.sigma.clotheswarehouse.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowGetDto {
    private UUID id;
    private Long clientId;
    private Double amount;
    private Timestamp beginDate;
    private Timestamp endDate;
    private boolean deleted;
}
