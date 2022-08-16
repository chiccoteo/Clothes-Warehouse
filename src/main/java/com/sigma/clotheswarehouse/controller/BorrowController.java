package com.sigma.clotheswarehouse.controller;

import com.sigma.clotheswarehouse.payload.ApiResponse;
import com.sigma.clotheswarehouse.payload.BorrowPostDto;
import com.sigma.clotheswarehouse.payload.BorrowUpdateDto;
import com.sigma.clotheswarehouse.service.BorrowService;
import com.sigma.clotheswarehouse.utils.AppConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.BASE_PATH + "/borrow")
@CrossOrigin(maxAge = 3600)
public class BorrowController {

    final private BorrowService borrowService;

    @PostMapping
    public HttpEntity<?> addBorrow(@RequestBody BorrowPostDto borrowPostDto){
        ApiResponse apiResponse = borrowService.addBorrow(borrowPostDto);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/getById/{id}")
    public HttpEntity<?> getBorrowById(@PathVariable UUID id){
        ApiResponse apiResponse = borrowService.getById(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }

    @GetMapping("/getAll")
    public HttpEntity<?> getBorrows(){
        ApiResponse apiResponse = borrowService.getBorrows();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }



    @PutMapping("/update/{id}")
    public HttpEntity<?> updateBorrow(@PathVariable UUID id, @RequestBody BorrowUpdateDto borrowUpdateDto){
        ApiResponse apiResponse = borrowService.updateBorrow(id, borrowUpdateDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }


    @DeleteMapping("/deleteBorrow/{id}")
    public HttpEntity<?> deleteClientBorrow(@PathVariable Long id){
        ApiResponse apiResponse = borrowService.deleteClientBorrow(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 400).body(apiResponse);
    }
}
