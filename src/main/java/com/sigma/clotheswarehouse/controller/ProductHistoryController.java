package com.sigma.clotheswarehouse.controller;

import com.sigma.clotheswarehouse.payload.ProductHistoryDTO;
import com.sigma.clotheswarehouse.service.ProductHistoryService;
import com.sigma.clotheswarehouse.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(AppConstant.BASE_PATH + "/productHistory")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class ProductHistoryController {

    private final ProductHistoryService productHistoryService;

    @GetMapping("/oneWeek")
    public HttpEntity<?> getProductHistoryOneWeek(){
        return productHistoryService.getOneWeek();
    }

    @GetMapping("/oneMonth")
    public HttpEntity<?> getProductHistoryOneMonth(){
        return productHistoryService.getOneMonth();
    }

    @GetMapping("/oneYear")
    public HttpEntity<?> getProductHistoryOneYear(){
        return productHistoryService.getOneYear();
    }

    @PostMapping
    public HttpEntity<?> create(@RequestBody ProductHistoryDTO productHistoryDTO){
        return productHistoryService.create(productHistoryDTO);
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable UUID id,@RequestBody ProductHistoryDTO productHistoryDTO){
        return productHistoryService.edit(id, productHistoryDTO);
    }

}
