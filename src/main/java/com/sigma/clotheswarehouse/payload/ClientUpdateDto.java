package com.sigma.clotheswarehouse.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientUpdateDto {
    private Long id;
    private String fio;
    private String phoneNumber;
    private String address;
}
